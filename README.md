# seq2bigtable

Read an HBase sequence file and write it to the bigtable emulator.

This now reads a Sequence file and prints data.

Type `sbt 'run nonempty-seqfile tmptable'` to run it.

Note the mysterious `InterruptedException` that occurs at exit time.
People on the tubes say this is just HADOOP-12829 and to ignore it...
https://stackoverflow.com/a/47285004

```aidl
2019-08-07 12:55:01 WARN  FileSystem:3066 - exception in the cleaner thread but it will continue to run
java.lang.InterruptedException
        at java.lang.Object.wait(Native Method)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
        at org.apache.hadoop.fs.FileSystem$Statistics$StatisticsDataReferenceCleaner.run(FileSystem.java:3060)
        at java.lang.Thread.run(Thread.java:748)
```

