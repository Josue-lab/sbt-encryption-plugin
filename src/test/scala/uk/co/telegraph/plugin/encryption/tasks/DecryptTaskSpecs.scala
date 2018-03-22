package uk.co.telegraph.plugin.encryption.tasks

import com.typesafe.config.ConfigFactory
import org.scalatest.{FreeSpec, Matchers}
import uk.co.telegraph.plugin.encryption.algebras.aws.KMSInterpreter

class DecryptTaskSpecs extends FreeSpec
  with Matchers
  with TaskSpec {
  "Given the DecryptTask " - {
    "when run, it should decrypt specified file" in {
      val encFile = "application_toenc.conf"
      val toDecFile = "application_todec.conf"
      val configFile = getResourceFileAbsolutePath(toDecFile)
      val destinationFile = getResourceFileAbsolutePath(encFile)
      DecryptTask(kmsKey, configFile, destinationFile, KMSInterpreter.interpreter()(logger)).runTask()

      val encConfig = ConfigFactory.load(encFile)
      encConfig.hasPath("foo.bar.value") shouldBe true
      encConfig.getInt("foo.bar.value") shouldBe 1
    }
  }
}
