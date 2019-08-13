# seq2bigtable

Read an HBase sequence file and write it to the bigtable emulator.

Type `sbt 'run empty-seqfile tmptable v'` to run it with an empty sequence file.
Type `sbt 'run nonempty-seqfile tmptable v'` to run it with an non-empty sequence file.
Use `-v` for verbose printing of records: `sbt 'run -v nonempty-seqfile tmptable v'`

It is also possible to create an assembly file for a self-contained jar that can be run on a suitable jvm.
Use `sbt assembly` to create it.  To run it, use something like

`java -jar target/scala-2.11/seq2bigtable-assembly-1.0.jar nonempty-seqfile tmptable v`

Note the mysterious `InterruptedException` that occurs at exit time.
People on the tubes say this is just HADOOP-12829 and to ignore it...
https://stackoverflow.com/a/47285004

```sbtshell
2019-08-07 12:55:01 WARN  FileSystem:3066 - exception in the cleaner thread but it will continue to run
java.lang.InterruptedException
        at java.lang.Object.wait(Native Method)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
        at org.apache.hadoop.fs.FileSystem$Statistics$StatisticsDataReferenceCleaner.run(FileSystem.java:3060)
        at java.lang.Thread.run(Thread.java:748)
```


Output of `sbt 'run empty-seqfile tmptable v'` to run it with an empty sequence file.
```sbtshell
[info] Running com.cphy.seq2bigtable.Seq2Bigtable empty-seqfile tmptable v                                                                      │
Reading Sequence file empty-seqfile and writing to table tmptable in column family: v                                                           │
bigtableEmulatorPort: 8086                                                                                                                      │
Data project: fake-project admin instance: fake-instance                                                                                        │
Admin project: fake-project admin instance: fake-instance                                                                                       │
Creating target table: tmptable and column family: v with maxVersions=1                                                                         │
2019-08-13 16:37:17 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where appli│
cable                                                                                                                                           │
2019-08-13 16:37:17 WARN  FileSystem:3066 - exception in the cleaner thread but it will continue to run                                         │
java.lang.InterruptedException                                                                                                                  │
        at java.lang.Object.wait(Native Method)                                                                                                 │
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)                                                                         │
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)                                                                         │
        at org.apache.hadoop.fs.FileSystem$Statistics$StatisticsDataReferenceCleaner.run(FileSystem.java:3060)                                  │
        at java.lang.Thread.run(Thread.java:748)                                                                                                │
[success] Total time: 4 s, completed Aug 13, 2019 4:37:17 PM
```
Output of `sbt 'run -v nonempty-seqfile tmptable v'` to run it with an non-empty sequence file.
```sbtshell
[info] Running com.cphy.seq2bigtable.Seq2Bigtable nonempty-seqfile tmptable v                                                                   │
Reading Sequence file nonempty-seqfile and writing to table tmptable in column family: v                                                        │
bigtableEmulatorPort: 8086                                                                                                                      │
Data project: fake-project admin instance: fake-instance                                                                                        │
Admin project: fake-project admin instance: fake-instance                                                                                       │
Target table exists: tmptable                                                                                                                   │
2019-08-13 16:38:03 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where appli│
cable                                                                                                                                           │
Key: dxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0vxxxxxxA4xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx3cxxxxxxxxxxxxxxxb146102400000014610240000001461110400000        │
Value: keyvalues={dxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0\x1Fvxxxxxx\x1FA4xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx3\x1Fcxxxxxxxxxxxxxxxb\x1F1461024000000\x1F1│
461024000000\x1F1461110400000/v:v/1563503181101/Put/vlen=191682/seqid=0/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx                         │
getFamilyArray; v
getQualifierArray: v                                                                                                                            │
timestampMicros: 1563503181101000                                                                                                               │
getValueArray: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx│
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx                 │
2019-08-13 16:38:04 WARN  FileSystem:3066 - exception in the cleaner thread but it will continue to run                                         │
java.lang.InterruptedException                                                                                                                  │
        at java.lang.Object.wait(Native Method)                                                                                                 │
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)                                                                         │
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)                                                                         │
        at org.apache.hadoop.fs.FileSystem$Statistics$StatisticsDataReferenceCleaner.run(FileSystem.java:3060)                                  │
        at java.lang.Thread.run(Thread.java:748)                                                                                                │
[success] Total time: 5 s, completed Aug 13, 2019 4:38:04 PM
```
