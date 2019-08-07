package com.cphy.seq2bigtable

import java.io.IOException

import org.apache.hadoop.hbase.io.ImmutableBytesWritable

//import scala.reflect._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
//import org.apache.hadoop.hbase.io.ImmutableBytesWritable
//import org.apache.hadoop.io.serializer.WritableSerialization
//import org.apache.hadoop.hive.hbase.ResultWritable
//import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.io.SequenceFile.Reader
//import org.apache.hadoop.io.Text
import org.apache.hadoop.hbase.client.Result
// Note that this requires hbase-server jar file:
import org.apache.hadoop.hbase.mapreduce.{MutationSerialization, ResultSerialization}

import collection.JavaConversions._

//import com.google.bigtable.v2.Mutation
//import com.google.bigtable.v2.Mutation.SetCell
//import com.google.common.collect.ImmutableList
//import com.google.protobuf.ByteString
//import java.nio.ByteBuffer

object Main extends App {

//  def toByteString(byteBuffer: ByteBuffer) = {
//    ByteString.copyFrom(byteBuffer.array())
//  }

  override def main(args: Array[String]): Unit = {
    super.main(args)
    println("Hello, World!")
    val seqFile: String = args(0)
    val tableName: String = args(1)
    val conf: Configuration = new Configuration()
//    val curSers: String = conf.get("io.serializations")
////    val xoo = this.getClass.getName
////    val foo = MutationSerialization.getClass.getName
////    val bar = ResultSerialization..getName
////    val ct = classTag[ResultSerialization]
////    val co = classOf[ResultSerialization].getName
////    val tt = typeTag[ResultSerialization]
//    val newSers: Array[String] = Array[String] (
//      curSers,
//      classOf[ResultSerialization].getName,
//      classOf[MutationSerialization].getName
//      "org.apache.hadoop.hbase.mapreduce.MutationSerialization",
//      "org.apache.hadoop.hbase.mapreduce.ResultSerialization"
//    )
    // conf.setStrings("io.serializations", newSers)
    // conf.setStrings("io.serializations", curSers, "org.apache.hadoop.hbase.mapreduce.MutationSerialization", "org.apache.hadoop.hbase.mapreduce.ResultSerialization")
    conf.setStrings(
      "io.serializations",
      conf.get("io.serializations"),
      classOf[MutationSerialization].getName,
      classOf[ResultSerialization].getName
    )
    println(s"debug: classOf[ResultSerialization].getName ${classOf[ResultSerialization].getName}")
    println(s"""debug: conf.get("io.serializations") ${conf.get("io.serializations")}""")
    // debug: conf.get("io.serializations")
    // org.apache.hadoop.io.serializer.WritableSerialization,
    // org.apache.hadoop.io.serializer.avro.AvroSpecificSerialization,
    // org.apache.hadoop.io.serializer.avro.AvroReflectSerialization,
    // org.apache.hadoop.hbase.mapreduce.ResultSerialization,
    // org.apache.hadoop.hbase.mapreduce.MutationSerialization
    // conf.setStrings("io.serializations", new String[]{conf.get("io.serializations"), MutationSerialization.class.getName(), ResultSerialization.class.getName()});invokekk
//    conf.setStrings("io.serializations", new String{conf.get("io.serializations"), MutationSerialization.class.getName(), ResultSerialization.class.getName()}) //invokekk

    try {
      val inFile: Path = new Path(seqFile)
      var reader: SequenceFile.Reader = null
      try {
        val key = new ImmutableBytesWritable()
//         val value = new ResultWritable()
//        val value = new Result()
        val value = new ImmutableBytesWritable() // Result()
        reader = new SequenceFile.Reader(conf, Reader.file(inFile), Reader.bufferSize(4096))
        while(reader.next(key, value)) {
          System.out.println("Key " + key + "Value " + value)
        }

      } finally {
        if(reader != null) {
          reader.close()
        }
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
      // TODO Auto-generated catch bloc
    }
  }

  /*

  def foo(): Unit = {
//    BigtableIO.Write write =
//      BigtableIO.write()
//        .withProjectId(options.getBigtableProjectId())
//        .withInstanceId(options.getBigtableInstanceId())
//        .withTableId(options.getBigtableTableId());

    /**
     * Translates {@link BigtableRow} to {@link Mutation}s along with a row key. The mutations are
     * {@link SetCell}s that set the value for specified cells with family name, column qualifier and
     * timestamp.
     */
    val key: ByteString = toByteString(row.getKey());
      public KV<ByteString, Iterable<Mutation>> apply(BigtableRow row) {
        ByteString key = toByteString(row.getKey());
        // BulkMutation doesn't split rows. Currently, if a single row contains more than 100,000
        // mutations, the service will fail the request.
        ImmutableList.Builder<Mutation> mutations = ImmutableList.builder();
        for (BigtableCell cell : row.getCells()) {
          SetCell setCell =
            SetCell.newBuilder()
              .setFamilyName(cell.getFamily().toString())
              .setColumnQualifier(toByteString(cell.getQualifier()))
              .setTimestampMicros(cell.getTimestamp())
              .setValue(toByteString(cell.getValue()))
              .build();
          mutations.add(Mutation.newBuilder().setSetCell(setCell).build());
        }
        return KV.of(key, mutations.build());
      }
    }
  }
   */

}

// https://stackoverflow.com/a/56285436
// https://stackoverflow.com/questions/44187041/could-not-find-a-serializer-for-the-value-class-org-apache-hadoop-hbase-client/56285436
// object HbaseDataExport extends LoggingTime{
//   def main(args: Array[String]): Unit = {
//     val con = SparkConfig.getProperties()
//     val sparkConf = SparkConfig.getSparkConf()
//     val sc = SparkContext.getOrCreate(sparkConf)
//     val config = HBaseConfiguration.create()
//     config.setStrings("io.serializations",
//       config.get("io.serializations"),
//       "org.apache.hadoop.hbase.mapreduce.MutationSerialization",
//       "org.apache.hadoop.hbase.mapreduce.ResultSerialization")
//     val path = "/Users/jhTian/Desktop/hbaseTimeData/part-m-00030"
//     val path1 = "hdfs://localhost:9000/hbaseTimeData/part-m-00030"
//
//     sc.newAPIHadoopFile(path1, classOf[SequenceFileInputFormat[Text, Result]], classOf[Text], classOf[Result], config).foreach(x => {
//       import collection.JavaConversions._
//       for (i <- x._2.listCells) {
//         logger.info(s"family:${Bytes.toString(CellUtil.cloneFamily(i))},qualifier:${Bytes.toString(CellUtil.cloneQualifier(i))},value:${Bytes.toString(CellUtil.cloneValue(i))}")
//       }
//     })
//     sc.stop()
//   }
// }
