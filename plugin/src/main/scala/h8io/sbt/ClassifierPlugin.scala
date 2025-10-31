package h8io.sbt

import sbt.*
import sbt.Keys.*

abstract class ClassifierPlugin extends AutoPlugin {
  protected def classifierConfig: Configuration
  protected def publishClassifier: SettingKey[Boolean]
  protected def classifier: String

  override def requires: Plugins = plugins.JvmPlugin
  override def trigger: PluginTrigger = noTrigger

  override def projectConfigurations: Seq[Configuration] = Seq(classifierConfig)

  override def projectSettings: Seq[Def.Setting[?]] = inConfig(classifierConfig)(Defaults.configSettings) ++ Seq(
    classifierConfig / sourceDirectory := (ThisProject / baseDirectory).value / "src" / classifier,
    classifierConfig / scalaSource := (classifierConfig / sourceDirectory).value / "scala",
    classifierConfig / resourceDirectory := (classifierConfig / sourceDirectory).value / "resources",
    Test / unmanagedSourceDirectories ++= (classifierConfig / unmanagedSourceDirectories).value,
    Test / unmanagedResourceDirectories ++= (classifierConfig / unmanagedResourceDirectories).value,
    publishClassifier := true,
    classifierConfig / packageBin / artifact :=
      (Compile / packageBin / artifact).value.withClassifier(Some(classifier)),
    classifierConfig / packageSrc / artifact :=
      (Compile / packageSrc / artifact).value.withClassifier(Some(classifier + "-sources")),
    classifierConfig / packageDoc / artifact :=
      (Compile / packageDoc / artifact).value.withClassifier(Some(classifier + "-javadoc")),
    artifacts ++= {
      if (publishClassifier.value)
        Seq(
          (classifierConfig / packageBin / artifact).value,
          (classifierConfig / packageSrc / artifact).value,
          (classifierConfig / packageDoc / artifact).value
        )
      else Nil
    },
    packagedArtifacts ++= {
      if (publishClassifier.value)
        Map(
          (classifierConfig / packageBin / artifact).value -> (classifierConfig / packageBin).value,
          (classifierConfig / packageSrc / artifact).value -> (classifierConfig / packageSrc).value,
          (classifierConfig / packageDoc / artifact).value -> (classifierConfig / packageDoc).value
        )
      else Map.empty[Artifact, File]
    }
  )
}
