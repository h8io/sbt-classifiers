import scala.xml.Elem

ThisBuild / organization := "io.h8.sbt"
ThisBuild / organizationName := "H8IO"
ThisBuild / organizationHomepage := Some(url("https://github.com/h8io/"))

ThisBuild / description := "SBT classifiers"
ThisBuild / licenses := List("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/h8io/sbt-classifiers"))
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / dynverSonatypeSnapshots := true
ThisBuild / dynverSeparator := "-"

ThisBuild / scalaVersion := "2.12.20"
// ThisBuild / crossScalaVersions += "3.7.2"
ThisBuild / scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 12)) => Seq("-Xsource:3")
  case _             => Nil
})
ThisBuild / javacOptions ++= Seq("-target", "8")

ThisBuild / developers := List(
  Developer(
    id = "eshu",
    name = "Pavel",
    email = "tjano.xibalba@gmail.com",
    url = url("https://github.com/h8io/")
  )
)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/h8io/sbt-classifiers"),
    "scm:git@github.com:h8io/sbt-classifiers.git"
  )
)

ThisBuild / dynverSonatypeSnapshots := true

val plugin = project
  .enablePlugins(SbtPlugin, ScoverageSummaryPlugin)
  .settings(
    name := "sbt-classifiers",
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.8.0"
        case _      => "2.0.0-RC6"
      }
    },
    libraryDependencies ++= Seq("org.scala-sbt" % "sbt" % (pluginCrossBuild / sbtVersion).value)
  )

val relocated = project
  .in(file("relocated"))
  .settings(
    name := "sbt-testkit",
    Compile / packageBin / publishArtifact := false,
    Compile / packageSrc / publishArtifact := false,
    Compile / packageDoc / publishArtifact := false,
    pomPostProcess := {
      case e: Elem if e.label == "project" =>
        val noPackaging = e.child.filterNot(_.label == "packaging")
        e.copy(child = noPackaging :+ <packaging>pom</packaging>)
      case other => other
    },
    pomExtra :=
      <distributionManagement>
      <relocation>
        <groupId>io.h8.sbt</groupId>
        <artifactId>sbt-classifiers</artifactId>
        <version>0.0.3</version>
        <message>Moved to io.h8.sbt:sbt-classifiers</message>
      </relocation>
    </distributionManagement>
  )

val root = project
  .in(file("."))
  .aggregate(plugin, relocated)
  .settings(publish / skip := true)
