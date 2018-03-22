package uk.co.telegraph.plugin.encryption.tasks

import com.typesafe.config.ConfigFactory
import org.scalatest.{FreeSpec, Matchers}
import uk.co.telegraph.plugin.encryption.algebras.aws.KMSInterpreter

class EncryptTaskSpecs extends FreeSpec
  with Matchers
  with TaskSpec {
  "Given the EncryptTask " - {
    "when run, it should encrypt specified file" in {
      val encFile = "application_toenc.conf"
      val toDecFile = "application_todec.conf"
      val configFile = getResourceFileAbsolutePath(encFile)
      val destinationFile = getResourceFileAbsolutePath(toDecFile)
      EncryptTask(kmsKey, configFile, destinationFile, KMSInterpreter.interpreter()(logger)).runTask()

      val encConfig = ConfigFactory.load(toDecFile)
      encConfig.hasPath("foo.bar.cipherConf") shouldBe true
    }
  }
}
