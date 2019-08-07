// The simplest possible sbt build file is just one line:

scalaVersion := "2.11.12"
// That is, to create a valid sbt build, all you've got to do is define the
// version of Scala you'd like your project to use.

// ============================================================================

// Lines like the above defining `scalaVersion` are called "settings" Settings
// are key/value pairs. In the case of `scalaVersion`, the key is "scalaVersion"
// and the value is "2.12.8"

// It's possible to define many kinds of settings, such as:

name := "seq2bigtable"
organization := "com.cphy"
version := "1.0"

// Note, it's not required for you to define these three settings. These are
// mostly only necessary if you intend to publish your library's binaries on a
// place like Sonatype or Bintray.


// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:

// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-1.x-shaded
// libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-1.x" % "1.12.0"
 libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-1.x-shaded" % "1.12.0"
// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-2.x
//libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-2.x" % "1.12.0"
//libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase" % "1.12.0"
// https://mvnrepository.com/artifact/org.apache.hbase/hbase-server
libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.4.10" // For ResultSerialization
// libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.1.5" // For ResultSerialization
//libraryDependencies += "org.apache.hbase" % "hbase-mapreduce" % "2.1.5" // For ResultSerialization
// libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.2.0" // For ResultSerialization
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


// Here, `libraryDependencies` is a set of dependencies, and by using `+=`,
// we're adding the cats dependency to the set of dependencies that sbt will go
// and fetch when it starts up.
// Now, in any Scala file, you can import classes, objects, etc, from cats with
// a regular import.

// TIP: To find the "dependency" that you need to add to the
// `libraryDependencies` set, which in the above example looks like this:

// "org.typelevel" %% "cats-core" % "1.6.0"

// You can use Scaladex, an index of all known published Scala libraries. There,
// after you find the library you want, you can just copy/paste the dependency
// information that you need into your build file. For example, on the
// typelevel/cats Scaladex page,
// https://index.scala-lang.org/typelevel/cats, you can copy/paste the sbt
// dependency from the sbt box on the right-hand side of the screen.

// IMPORTANT NOTE: while build files look _kind of_ like regular Scala, it's
// important to note that syntax in *.sbt files doesn't always behave like
// regular Scala. For example, notice in this build file that it's not required
// to put our settings into an enclosing object or class. Always remember that
// sbt is a bit different, semantically, than vanilla Scala.

// ============================================================================

// Most moderately interesting Scala projects don't make use of the very simple
// build file style (called "bare style") used in this build.sbt file. Most
// intermediate Scala projects make use of so-called "multi-project" builds. A
// multi-project build makes it possible to have different folders which sbt can
// be configured differently for. That is, you may wish to have different
// dependencies or different testing frameworks defined for different parts of
// your codebase. Multi-project builds make this possible.

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

mainClass in assembly := Some("com.cphy.seq2bigtable.Main")

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
