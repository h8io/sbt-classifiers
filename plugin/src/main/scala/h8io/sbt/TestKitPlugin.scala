package h8io.sbt

import sbt.*
import sbt.Keys.*

object TestKitPlugin extends AutoPlugin {
  override def requires: Plugins = plugins.JvmPlugin
  override def trigger: PluginTrigger = noTrigger

  object autoImport {
    val TestKit: Configuration = config("testkit").extend(Compile)

    val testkitPublishClassifier = settingKey[Boolean]("Whether to publish the testkit classifier artifact.")
    val testkitClassifier = settingKey[String]("Classifier name to use for the testkit artifact.")
  }
  import autoImport.*

  override def projectConfigurations: Seq[Configuration] = Seq(TestKit)

  override def projectSettings: Seq[Def.Setting[?]] = inConfig(TestKit)(Defaults.configSettings) ++ Seq(
    TestKit / sourceDirectory := (ThisProject / baseDirectory).value / "src" / "testkit",
    TestKit / scalaSource := (TestKit / sourceDirectory).value / "scala",
    TestKit / resourceDirectory := (TestKit / sourceDirectory).value / "resources",
    Test / unmanagedSourceDirectories ++= (TestKit / unmanagedSourceDirectories).value,
    Test / unmanagedResourceDirectories ++= (TestKit / unmanagedResourceDirectories).value,
    testkitPublishClassifier := false,
    testkitClassifier := "testkit",
    TestKit / packageBin / artifact :=
      (Compile / packageBin / artifact).value.withClassifier(Some(testkitClassifier.value)),
    TestKit / packageSrc / artifact :=
      (Compile / packageSrc / artifact).value.withClassifier(Some(testkitClassifier.value)),
    TestKit / packageDoc / artifact :=
      (Compile / packageDoc / artifact).value.withClassifier(Some(testkitClassifier.value)),
    artifacts ++= {
      if (testkitPublishClassifier.value)
        Seq(
          (TestKit / packageBin / artifact).value,
          (TestKit / packageSrc / artifact).value,
          (TestKit / packageDoc / artifact).value
        )
      else Nil
    },
    packagedArtifacts ++= {
      if (testkitPublishClassifier.value)
        Map(
          (TestKit / packageBin / artifact).value -> (TestKit / packageBin).value,
          (TestKit / packageSrc / artifact).value -> (TestKit / packageSrc).value,
          (TestKit / packageDoc / artifact).value -> (TestKit / packageDoc).value
        )
      else Map.empty[Artifact, File]
    }
  )
}
