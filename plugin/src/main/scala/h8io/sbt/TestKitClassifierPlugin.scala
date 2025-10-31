package h8io.sbt

import sbt.*

object TestKitClassifierPlugin extends ClassifierPlugin {
  object autoImport {
    val TestKitClassifierConfiguration = config("testkit").extend(Compile)

    val testkitPublishClassifier = settingKey[Boolean]("Whether to publish the testkit classifier artifact.")
  }
  import autoImport.*

  protected def classifierConfig: sbt.Configuration = TestKitClassifierConfiguration

  protected def publishClassifier: SettingKey[Boolean] = testkitPublishClassifier

  protected def classifier: String = "testkit"
}
