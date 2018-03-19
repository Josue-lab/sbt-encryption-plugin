package uk.co.telegraph.plugin.encryption.utils

import java.nio.ByteBuffer

import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.{DecryptRequest, DecryptResult, EncryptRequest, EncryptResult}
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers, OneInstancePerTest}

class ConfigEncryptorSpecs extends FreeSpec
  with Matchers
  with MockFactory
  with OneInstancePerTest
{
  "Given the ConfigEncryptor " - {
    val config = ConfigFactory.load("secret_application")
    val configPath = "foo"
    val key = "BFDCD9273019F2693E33B2049DF6A0672662FE8D8882B8092957B525CCD2311A"
    val plainText = "plain text"
    val plainTextBlob = ByteBuffer.wrap(plainText.getBytes())
    val base64PlainTextBlob = ByteBuffer.wrap(base64Encode(plainTextBlob).getBytes())
    val decryptResult = new DecryptResult().withPlaintext(base64PlainTextBlob)
    val encryptedValue = "lookhowencryptedthisisomg"
    val encryptedValueBlob = ByteBuffer.wrap(encryptedValue.getBytes())
    val encryptResult = new EncryptResult().withCiphertextBlob(encryptedValueBlob)

    val awsKmsClientMock = mock[AWSKMS]
    (awsKmsClientMock.decrypt(_: DecryptRequest)).expects(*).returning(decryptResult).anyNumberOfTimes()
    (awsKmsClientMock.encrypt(_: EncryptRequest)).expects(*).returning(encryptResult).anyNumberOfTimes()

    val configEncrypter = new AWSConfigEncryptor(awsKmsClientMock)
    "when encrypting with given key, it should return the expected configuration." in {
      val newConfig = configEncrypter.encrypt(config, configPath, key)

      newConfig.getString(s"$configPath.$EncryptedConfigField") shouldBe base64Encode(ByteBuffer.wrap(encryptedValue.getBytes))
    }
    "when decrypting with given key, it should return the expected configuration." in {
      val newConfig = configEncrypter.decrypt(config, configPath, key)

      newConfig.getString(configPath) shouldBe plainText
    }
  }
}
