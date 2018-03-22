package uk.co.telegraph.plugin.encryption.utils

import java.nio.ByteBuffer

import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.{DecryptRequest, DecryptResult, EncryptRequest, EncryptResult}
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers, OneInstancePerTest}
import uk.co.telegraph.plugin.encryption.utils.aws.KMSConfigEncryptor

class ConfigEncryptorSpecs extends FreeSpec
  with Matchers
  with MockFactory
  with OneInstancePerTest
{
  "Given the ConfigEncryptor " - {
    val configPath = "foo"
    val key = "(*&(&F(*SUH(UNGS(UKIUYSGI"
    val plainText = "plain text"
    val plainTextBlob = ByteBuffer.wrap(plainText.getBytes())
    val decryptResult = new DecryptResult().withPlaintext(plainTextBlob)
    val encryptedValue = "lookhowencryptedthisisomg"
    val encryptedValueBlob = ByteBuffer.wrap(encryptedValue.getBytes())
    val encryptResult = new EncryptResult().withCiphertextBlob(encryptedValueBlob)

    val awsKmsClientMock = mock[AWSKMS]
    (awsKmsClientMock.decrypt(_: DecryptRequest)).expects(*).returning(decryptResult).anyNumberOfTimes()
    (awsKmsClientMock.encrypt(_: EncryptRequest)).expects(*).returning(encryptResult).anyNumberOfTimes()

    val configEncrypter = new KMSConfigEncryptor(awsKmsClientMock)
    "when encrypting with given key, it should return the expected configuration." in {
      val config = ConfigFactory.load("secret_application")
      val newConfig = configEncrypter.encrypt(config, configPath, key)

      newConfig.getString(s"$configPath.$EncryptedConfigField") shouldBe base64Encode(ByteBuffer.wrap(encryptedValue.getBytes))
    }
    "when decrypting with given key, it should return the expected configuration." in {
      val config = ConfigFactory.load("secret_todec_application.conf")
      val newConfig = configEncrypter.decrypt(config, configPath)

      newConfig.getString(configPath) shouldBe plainText
    }
  }
}
