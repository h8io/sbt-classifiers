package h8io.sbt

import sbt.*

object CatsClassifierPlugin extends ArtifactClassifierPlugin {
  protected def artifactClassifier: String = "cats"

  object autoImport {
    object cats {
      val Variant: Configuration = config(artifactClassifier).extend(Compile).describedAs("Cats variant")

      val artifactsEnabled = settingKey[Boolean]("Whether to publish the cats classifier artifact.")
    }
  }
  import autoImport.*

  protected def classifiedConfig: sbt.Configuration = cats.Variant

  protected def classifierArtifactsEnabled: SettingKey[Boolean] = cats.artifactsEnabled
}
