package h8io.sbt

import sbt.*
import sbt.Keys.*

object CatsClassifierPlugin extends ClassifierPlugin {
  object autoImport {
    val CatsClassifierConfiguration = config("cats").extend(Compile)

    val catsPublishClassifier = settingKey[Boolean]("Whether to publish the cats classifier artifact.")
  }
  import autoImport.*

  protected def classifierConfig: sbt.Configuration = CatsClassifierConfiguration

  protected def publishClassifier: SettingKey[Boolean] = catsPublishClassifier

  protected def classifier: String = "cats"
}
