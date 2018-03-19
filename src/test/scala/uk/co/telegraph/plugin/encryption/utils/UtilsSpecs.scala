package uk.co.telegraph.plugin.encryption.utils

import com.typesafe.config.ConfigFactory
import org.scalatest.{FreeSpec, Matchers}

class UtilsSpecs extends FreeSpec
  with Matchers {
  "Given the utils package object " - {
    "when replacing config blocks, it should be appearing on the resulting config" in {
      val config = ConfigFactory.load("tobereplaced_application.conf")
      val configPath = "foo.bar.this"
      val newValue = "new value"
      val configToMerge = ConfigFactory.parseString(s"""{"foo":{"bar"{"this":"$newValue"}}}""")

      val newConfig = replaceConfigField(config, configPath, configToMerge)

      newConfig.getString(configPath) shouldBe newValue
    }
    "when getting the configuration plain text, it should be the expected one" in {
      val config = ConfigFactory.load("plaintext_application.conf")
      val configPath = "here.is.some.config"
      val expectedValue = """{"bar":"2","far":true,"foo":1}"""

      val configPlainText = getConfigPlainText(config, configPath)

      configPlainText shouldBe expectedValue
    }
    "when getting the configuration plain text for a wrong path, it should generate an exception" in {
      val config = ConfigFactory.load("plaintext_application.conf")
      val configPath = "bad.bad.path"

      assertThrows[Exception] {
        getConfigPlainText(config, configPath)
      }
    }
  }
}
