scalaVersion := "2.11.12"
scalacOptions += "-deprecation"

name := "seq2bigtable"
organization := "com.cphy"
version := "1.0"

// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-1.x-shaded
// libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-1.x" % "1.12.0"
// libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-1.x-shaded" % "1.12.0"
// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-2.x
libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-2.x" % "1.12.0"
//libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase" % "1.12.0"
// https://mvnrepository.com/artifact/org.apache.hbase/hbase-server
//libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.4.10" // For ResultSerialization
//libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.4.10" // For ResultSerialization
//libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.1.5" // For ResultSerialization
//libraryDependencies += "org.apache.hbase" % "hbase-mapreduce" % "2.1.5" // For ResultSerialization
libraryDependencies += "org.apache.hbase" % "hbase-mapreduce" % "2.2.0" // For ResultSerialization
//libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.2.0" // For ResultSerialization
//libraryDependencies += "com.google.cloud" % "google-cloud-bigtable" % "0.101.0"
// https://mvnrepository.com/artifact/org.apache.hive/hive-hbase-handler
//libraryDependencies += "org.apache.hive" % "hive-hbase-handler" % "1.2.1"
//libraryDependencies += "org.apache.hive" % "hive-hbase-handler" % "1.2.2"
//libraryDependencies += "org.apache.hive" % "hive-hbase-handler" % "2.3.5"
//libraryDependencies += "org.apache.hive" % "hive-hbase-handler" % "3.1.1"
// https://mvnrepository.com/artifact/org.spark-project.hive/hive-hbase-handler
//libraryDependencies += "org.spark-project.hive" % "hive-hbase-handler" % "1.2.1.spark2"

// https://mvnrepository.com/artifact/org.pentaho/pentaho-aggdesigner-algorithm
//libraryDependencies += "org.pentaho" % "pentaho-aggdesigner-algorithm" % "5.1.5-jhyde" // % Test

// See: https://stackoverflow.com/a/49242622
//resolvers += Resolver.mavenLocal
//resolvers += "Cascading repo" at "http://conjars.org/repo"

// Here's a quick glimpse of what a multi-project build looks like for this
// build, with only one "subproject" defined, called `root`:

// lazy val root = (project in file(".")).
//   settings(
//     inThisBuild(List(
//       organization := "ch.epfl.scala",
//       scalaVersion := "2.12.8"
//     )),
//     name := "hello-world"
//   )

// To learn more about multi-project builds, head over to the official sbt
// documentation at http://www.scala-sbt.org/documentation.html

mainClass in assembly := Some("com.cphy.seq2bigtable.Seq2Bigtable")

enablePlugins(JavaAppPackaging)

//test in (Test, assembly) := {}
test in assembly := {}

assemblyMergeStrategy in assembly := {
  // https://stackoverflow.com/questions/54625572/sbt-assembly-errordeduplicate-different-file-contents-found-in-io-netty-versio#comment96046026_54625572
  case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case PathList("javax", xs @ _*) => MergeStrategy.first
  case PathList("org", xs @ _*)   => MergeStrategy.first
  case "mozilla/public-suffix-list.txt" => MergeStrategy.first
//  case PathList("javolution", xs @ _*)   => MergeStrategy.discard
//  case PathList("org", xs @ _*)   => MergeStrategy.first
//  // Discard module_info.class if using Java 8.  Revisit when upgraded. See: https://stackoverflow.com/a/55557287
//  // https://github.com/sbt/sbt-assembly/issues/370#issuecomment-496502318
//  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
