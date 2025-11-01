package h8io.sbt

import sbt.*
import sbt.Keys.*

abstract class ArtifactClassifierPlugin extends AutoPlugin {
  protected def classifiedConfig: Configuration
  protected def classifierArtifactsEnabled: SettingKey[Boolean]
  protected def artifactClassifier: String

  override def requires: Plugins = plugins.JvmPlugin
  override def trigger: PluginTrigger = noTrigger

  override def projectConfigurations: Seq[Configuration] = Seq(classifiedConfig)

  override def projectSettings: Seq[Def.Setting[?]] =
    inConfig(classifiedConfig)(Defaults.configSettings) ++ Seq(
      classifiedConfig / sourceDirectory := (ThisProject / baseDirectory).value / "src" / artifactClassifier,
      classifiedConfig / scalaSource := (classifiedConfig / sourceDirectory).value / "scala",
      classifiedConfig / resourceDirectory := (classifiedConfig / sourceDirectory).value / "resources",
      Test / unmanagedSourceDirectories ++= (classifiedConfig / unmanagedSourceDirectories).value,
      Test / unmanagedResourceDirectories ++= (classifiedConfig / unmanagedResourceDirectories).value,
      classifierArtifactsEnabled := true,
      classifiedConfig / packageBin / artifact :=
        (Compile / packageBin / artifact).value.withClassifier(Some(artifactClassifier)),
      classifiedConfig / packageSrc / artifact :=
        (Compile / packageSrc / artifact).value.withClassifier(Some(artifactClassifier + "-sources")),
      classifiedConfig / packageDoc / artifact :=
        (Compile / packageDoc / artifact).value.withClassifier(Some(artifactClassifier + "-javadoc")),
      artifacts ++= {
        if (classifierArtifactsEnabled.value)
          Seq(
            (classifiedConfig / packageBin / artifact).value,
            (classifiedConfig / packageSrc / artifact).value,
            (classifiedConfig / packageDoc / artifact).value
          )
        else Nil
      },
      packagedArtifacts ++= {
        if (classifierArtifactsEnabled.value)
          Map(
            (classifiedConfig / packageBin / artifact).value -> (classifiedConfig / packageBin).value,
            (classifiedConfig / packageSrc / artifact).value -> (classifiedConfig / packageSrc).value,
            (classifiedConfig / packageDoc / artifact).value -> (classifiedConfig / packageDoc).value
          )
        else Map.empty[Artifact, File]
      }
    )
}
