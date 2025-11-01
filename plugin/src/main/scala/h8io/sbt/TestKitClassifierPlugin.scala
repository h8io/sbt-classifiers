package h8io.sbt

import sbt.*

object TestKitClassifierPlugin extends ArtifactClassifierPlugin {
  protected def artifactClassifier: String = "testkit"

  object autoImport {
    object testkit {
      val Variant: Configuration = config(artifactClassifier).extend(Compile).describedAs("TestKit variant")

      val artifactsEnabled = settingKey[Boolean]("Whether to publish the testkit classifier artifact.")
    }
  }
  import autoImport.*

  protected def classifiedConfig: sbt.Configuration = testkit.Variant

  protected def classifierArtifactsEnabled: SettingKey[Boolean] = testkit.artifactsEnabled
}
