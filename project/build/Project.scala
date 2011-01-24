import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val scalatoolsRelease = "Scala Tools Release" at "http://scala-tools.org/repo-releases/"
  val scalatoolsSnapshot = "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"
  val mavenRepository = "maven" at "http://repo1.maven.org/maven2/"
  val javaNetRepo = "Java Dot Net" at "http://download.java.net/maven/2/"
  val sonatype = "Sonatype" at "http://oss.sonatype.org/content/repositories/releases"
  val riReleases = "RI Releases" at "http://maven.research-infrastructures.eu/nexus/content/repositories/releases"

  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  
//  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.2" % "test"
  val specsdep = "org.scala-tools.testing" %% "specs" % "1.6.7-SNAPSHOT" % "test->default"

  val guice2 = "com.google.inject" % "guice" % "2.0"
  val guiceScala = "uk.me.lings" % "scala-guice_2.8.0" % "0.1"

  val asm = "asm" % "asm" % "3.3"
  val asmc = "asm" % "asm-commons" % "3.3"
  val asmu = "asm" % "asm-util" % "3.3"

  val commonsio = "commons-io" % "commons-io" % "2.0.1"
}
