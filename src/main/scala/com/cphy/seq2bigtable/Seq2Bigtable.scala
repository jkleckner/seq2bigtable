package com.cphy.seq2bigtable

import java.io.IOException

import com.google.api.gax.batching.Batcher
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.io.SequenceFile.Reader
import org.apache.hadoop.hbase.client.Result
// Note that this requires hbase-mapreduce jar file:
import org.apache.hadoop.hbase.mapreduce.{MutationSerialization, ResultSerialization}

import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest
import com.google.cloud.bigtable.admin.v2.{BigtableTableAdminClient, BigtableTableAdminSettings}
import com.google.cloud.bigtable.data.v2.{BigtableDataClient, BigtableDataSettings}
import com.google.cloud.bigtable.admin.v2.models.GCRules

import collection.JavaConversions._

import com.google.protobuf.ByteString

object Seq2Bigtable {

  // Usage: com.cphy.seq2bigtable.Seq2Bigtable <path-to-seq-file> <table-where-to-import> <column_family>
  // Start and set the bigtable emulator environment variable BIGTABLE_EMULATOR_HOST before running with:
  // gcloud beta emulators bigtable start &
  // $(gcloud beta emulators bigtable env-init)

  def toByteString(byteArray: Array[Byte]): ByteString = {
    ByteString.copyFrom(byteArray)
  }

  def main(args: Array[String]): Unit = {
    val appConfig: Config = ConfigFactory.load()
    val projectId = appConfig.getString("seq2bigtable.project")
    val instanceId = appConfig.getString("seq2bigtable.instance")
    val tableName = appConfig.getString("seq2bigtable.table")
    val columnFamily = appConfig.getString("seq2bigtable.column_family")
    val seqFile = appConfig.getString("seq2bigtable.file")
    val printData = appConfig.getBoolean("seq2bigtable.verbose")
    println(s"projectId: $projectId instanceId: $instanceId")
    println(s"Reading Sequence file $seqFile and writing to table $tableName in column family: $columnFamily")
    val debugPrintTakeBytes: Int = 256

    val hadoopConfig: Configuration = new Configuration()
    hadoopConfig.setStrings(
      "io.serializations",
      hadoopConfig.get("io.serializations"),
      classOf[MutationSerialization].getName,
      classOf[ResultSerialization].getName
    )
    //  println(s"debug: classOf[ResultSerialization].getName ${classOf[ResultSerialization].getName}")
    //  println(s"""debug: hadoopConfig.get("io.serializations") ${hadoopConfig.get("io.serializations")}""")

    try {
      // hbase reader:
      val inFile: Path = new Path(seqFile)
      var reader: SequenceFile.Reader = null
      // bigtable writer:
      val dataSettings: BigtableDataSettings = BigtableDataSettings
        .newBuilder
        .setProjectId(projectId)
        .setInstanceId(instanceId)
        .build()
      val dataClient: BigtableDataClient = BigtableDataClient.create(dataSettings)

      val adminSettings: BigtableTableAdminSettings = BigtableTableAdminSettings
        .newBuilder
        .setProjectId(projectId)
        .setInstanceId(instanceId)
        .build()
      val adminClient: BigtableTableAdminClient = BigtableTableAdminClient.create(adminSettings)

      val bulkMutationBatcher: Batcher[RowMutationEntry, Void] = dataClient.newBulkMutationBatcher(tableName)

      println(s"Data project: ${adminClient.getProjectId} admin instance: ${adminClient.getInstanceId}")
      println(s"Admin project: ${adminClient.getProjectId} admin instance: ${adminClient.getInstanceId}")

      try {
        if (adminClient.exists(tableName)) {
          println(s"Target table exists: $tableName")
        } else {
          println(s"Creating target table: $tableName and column family: $columnFamily with maxVersions=1")
          adminClient
            .createTable(
              CreateTableRequest
                .of(tableName)
                .addFamily(columnFamily, GCRules.GCRULES.maxVersions(1))
            )
        }
        val key = new ImmutableBytesWritable()
        reader = new SequenceFile.Reader(hadoopConfig, Reader.file(inFile), Reader.bufferSize(4096))
        while(reader.next(key)) {
          val rowKeyBytes = key.get
          val value: Result = reader
            .getCurrentValue(new Result)
            .asInstanceOf[Result]
          if (printData) {
            println("Key: " + new String(key.get()))
            println("Value: " + value.toString.take(debugPrintTakeBytes))
          }
          for {
            cell <- value.listCells()
          } {
            val familyString: String = new String(cell.getFamilyArray)
            val qualifierBytes: Array[Byte] = cell.getQualifierArray
            val timestampMicros: Long = cell.getTimestamp * 1000
            val valueBytes: Array[Byte] = cell.getValueArray
            // Debug printing
            if (printData) {
              println(s"getFamilyArray; $familyString")
              println(s"getQualifierArray: ${new String(qualifierBytes)}")
              println(s"timestampMicros: $timestampMicros")
              println(s"getValueArray: ${new String(valueBytes.take(debugPrintTakeBytes))}")
            }
            // Note assuming that table and column families are already created
            val rowMutation = RowMutationEntry
              .create(toByteString(rowKeyBytes))
              .setCell(familyString, toByteString(qualifierBytes), timestampMicros, toByteString(valueBytes))
//            dataClient.mutateRow(rowMutation) // Immediate execution version
            bulkMutationBatcher.add(rowMutation)
          }
        }
      } catch {
        case t: Throwable =>
          println(s"Caught Throwable: Message: ${t.getMessage}")
          t.printStackTrace()
          throw t
      } finally {
        if(reader != null) {
          reader.close()
          bulkMutationBatcher.close()
        }
      }
    } catch {
      case _: InterruptedException =>
        // Ignore the InterruptedException
        ()
      case e: IOException =>
        e.printStackTrace()
      case t: Throwable =>
        t.printStackTrace()
    }
  }
}
