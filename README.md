# seq2bigtable

Read an HBase sequence file and write it to the bigtable emulator.

This is in a non-working state and is being used to communicate with developers about the task.

Type `sbt 'run empty-seqfile tmptable'` to observe the error message:

```
java.io.IOException: wrong value class:  is not class org.apache.hadoop.hbase.client.Result
        at org.apache.hadoop.io.SequenceFile$Reader.next(SequenceFile.java:2379)
        at com.cphy.seq2bigtable.Main$.main(Main.scala:83)
...
```
which makes sense since the `ImmutableBytesWritable` is not a `ResultWritable`.
