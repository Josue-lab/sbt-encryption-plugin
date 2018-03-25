package uk.co.telegraph.plugin.encryption.utils.aws

import java.nio.ByteBuffer

import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.{DecryptRequest, EncryptRequest}
import com.typesafe.config.{Config, ConfigFactory}
import uk.co.telegraph.plugin.encryption.utils._

class KMSConfigEncryptor(awskms: AWSKMS) extends ConfigEncryptor {
  type EncDecOperation = (ByteBuffer) => Config
  private def configEncDecOps(config: Config, configPath: String, op: EncDecOperation): Config = {
    val plainText = getConfigPlainText(config, configPath)
    replaceConfigField(config, configPath, op(ByteBuffer.wrap(plainText.getBytes())))
  }

  override def encrypt(config: Config, configPath: String, key: String): Config = {
    configEncDecOps(config, configPath, (plainTextBlob: ByteBuffer) => {
      val encryptRequest = new EncryptRequest()
        .withPlaintext(plainTextBlob)
        .withKeyId(key)
      val cipherConf = base64Encode(awskms.encrypt(encryptRequest).getCiphertextBlob())
      ConfigFactory.parseString(s"""{$configPath:{$IsEncrypted = true, $EncryptedConfigField = "$cipherConf"}}""")
    })
  }

  override def decrypt(config: Config, configPath: String): Config = {
    configEncDecOps(config, s"$configPath.$EncryptedConfigField", (cipherTextBlob: ByteBuffer) => {
      val decryptRequest = new DecryptRequest()
        .withCiphertextBlob(base64Decode(cipherTextBlob))
      val plainText = new String(awskms.decrypt(decryptRequest).getPlaintext().array(), "UTF-8")
      ConfigFactory.parseString(s"""{$configPath = $plainText}""")
    })
  }
}
object KMSConfigEncryptor extends KMSConfigEncryptor(awsClient)
