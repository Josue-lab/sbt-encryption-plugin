package uk.co.telegraph.plugin.encryption.utils

import org.scalatest.{FreeSpec, Matchers}

class ConfigParserSpecs extends FreeSpec
  with Matchers
{
  "Given the ConfigParser " - {
    val configParser = ConfigParser()
    "when retrieving the encrypted blocks, it should be the the ones with the 'isEncrypted' flag set to true" in {
      val configBlocks = configParser.encryptedConfiguration(getConfig(Some("secret_application.conf")))

      configBlocks should contain("foo")
      configBlocks should contain("look.how.nested.this.config.is.why.because")
      configBlocks should not contain("watch.out.this.is.not.encrypted")
    }
  }
}
