scalaVersion := "2.11.12"
scalacOptions += "-deprecation"

name := "seq2bigtable"
organization := "com.cphy"
// version := "1.0"

// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-2.x
libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-2.x" % "1.12.0"
libraryDependencies += "org.apache.hbase" % "hbase-mapreduce" % "2.2.0" // For ResultSerialization
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
//libraryDependencies += "com.google.cloud" % "google-cloud-bigtable" % "0.101.0"

mainClass in assembly := Some("com.cphy.seq2bigtable.Seq2Bigtable")

enablePlugins(JavaAppPackaging)

//test in (Test, assembly) := {}
test in assembly := {}

assemblyMergeStrategy in assembly := {
  // https://stackoverflow.com/questions/54625572/sbt-assembly-errordeduplicate-different-file-contents-found-in-io-netty-versio#comment96046026_54625572
  case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case "META-INF/native/libnetty_transport_native_kqueue_x86_64.jnilib" => MergeStrategy.first
  case "META-INF/native/liborg_apache_hbase_thirdparty_netty_transport_native_epoll_x86_64.so" => MergeStrategy.first
  case x if x.startsWith("org/apache/hadoop") => MergeStrategy.first
  case x if x.startsWith("org/apache/hbase") => MergeStrategy.first
  case "mozilla/public-suffix-list.txt" => MergeStrategy.first
//  // Discard module_info.class if using Java 8.  Revisit when upgraded. See: https://stackoverflow.com/a/55557287
//  // https://github.com/sbt/sbt-assembly/issues/370#issuecomment-496502318
//  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
