import com.typesafe.config._
import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.gzip.Import.gzip
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.rjs.Import.{RjsKeys, rjs}
import com.typesafe.sbt.web.SbtWeb.autoImport.{Assets, pipelineStages}
import play.sbt.PlayImport._
import sbt.Keys._
import sbt._

object Common {
  def appName = "oitavo"
	
  // Common settings for every project
  def settings (theName: String) = Seq(
    name := theName,
    organization := "com.myweb",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    doc in Compile <<= target.map(_ / "none"),
    scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:reflectiveCalls", "-language:postfixOps", "-language:implicitConversions"),
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
      "Atlassian Releases" at "https://maven.atlassian.com/public/",
      Resolver.jcenterRepo,
      Resolver.sonatypeRepo("snapshots")
    )
  )

  // Settings for the app, i.e. the root project
  def appSettings (messagesFilesFrom: Seq[String]) = settings(appName) ++: Seq(
    javaOptions += s"-Dconfig.resource=root-dev.conf",
    messagesGenerator in Compile := messagesGenerate(messagesFilesFrom, baseDirectory.value, resourceManaged.value, streams.value.log),
    resourceGenerators in Compile <+= (messagesGenerator in Compile)
  )

  // Settings for every module, i.e. for every subproject
  def moduleSettings (module: String) = settings(module) ++: Seq(
    javaOptions += s"-Dconfig.resource=$module-dev.conf",
    sharedConfFilesReplicator in Compile := sharedConfFilesReplicate(baseDirectory.value / ".." / "..", resourceManaged.value, streams.value.log),
    resourceGenerators in Compile <+= (sharedConfFilesReplicator in Compile)
  )
  // Settings for every service, i.e. for admin and web subprojects
  def serviceSettings (module: String, messagesFilesFrom: Seq[String]) = moduleSettings(module) ++: Seq(
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    pipelineStages := Seq(rjs, digest, gzip),
    RjsKeys.mainModule := s"main-$module",
    messagesGenerator in Compile := messagesGenerate(messagesFilesFrom, baseDirectory.value / ".." / "..", resourceManaged.value, streams.value.log),
    resourceGenerators in Compile <+= (messagesGenerator in Compile)
  )
	
  val commonDependencies = Seq(
    cache,
    ws,
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
    "org.webjars" % "requirejs" % "2.3.1",
    "com.iheart" %% "ficus" % "1.3.2",
    "com.typesafe.play" %% "anorm" % "2.5.0",
    "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3",
    jdbc,
    evolutions
    // Add here more common dependencies:
    // jdbc,
    // anorm,
    // ...
  )
	
	
	
  /*
  * Utilities to replicate shared.*.conf files
  */
	
  lazy val sharedConfFilesReplicator = taskKey[Seq[File]]("Replicate shared.*.conf files.")
	
  def sharedConfFilesReplicate (rootDir: File, managedDir: File, log: Logger): Seq[File] = {
    val files = ((rootDir / "conf") ** "shared.*.conf").get
    val destinationDir = managedDir / "conf"
    destinationDir.mkdirs()
    files.map { file =>
      val destinationFile = destinationDir / file.getName()
      IO.copyFile(file, destinationFile)
      file
    }
  }
	
  /*
  * Utilities to generate the messages files
  */
	
  val conf = ConfigFactory.parseFile(new File("conf/shared.dev.conf")).resolve()
  val langs = scala.collection.JavaConversions.asScalaBuffer(conf.getStringList("play.i18n.langs"))
	
  lazy val messagesGenerator = taskKey[Seq[File]]("Generate the messages resource files.")
	
  def messagesGenerate (messagesFilesFrom: Seq[String], rootDir: File, managedDir: File, log: Logger): Seq[File] = {
    val destinationDir = managedDir / "conf"
    destinationDir.mkdirs()
    val files = langs.map { lang =>
      val messagesFilename = s"messages.$lang"
      val originFiles = messagesFilesFrom.map(subproject => rootDir / "modules" / subproject / "conf" / "messages" / messagesFilename)
      val destinationFile = destinationDir / messagesFilename
      IO.write(destinationFile, "## GENERATED FILE ##\n\n", append = false)
      originFiles.map { file =>
        IO.writeLines(destinationFile, lines = IO.readLines(file), append = true)
      }
      destinationFile
    }
    log.info("Generated messages files")
    files
  }
	
}