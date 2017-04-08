name := """InSight"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.7",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.scalaj" %% "scalaj-http" % "2.3.0"
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
