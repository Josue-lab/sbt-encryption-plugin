package uk.co.telegraph.plugin.encryption.utils

import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.{FreeSpec, Matchers}

class ConfigWriterSpecs extends FreeSpec
  with Matchers
{
  "Given the ConfigWriter " - {
    "when saving a config to file, it should exist and contain same values" in {
      val config = ConfigFactory.load("configwriter_application.conf")
      val configDestinationFile = "configwriter_destination_application.conf"
      val filePath = new File(s"src/test/resources/$configDestinationFile").getAbsolutePath

      ConfigWriter.writeToFile(config, filePath)

      val resultConfig = ConfigFactory.load(configDestinationFile)
      resultConfig.getString("foo") shouldBe "bar"
    }
  }

}
