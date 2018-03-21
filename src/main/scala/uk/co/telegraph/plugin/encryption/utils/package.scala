package uk.co.telegraph.plugin.encryption

import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}

import scala.util.{Success, Try}

package object utils {
  type ConfigPath = String
  final val IsEncrypted = "isEncrypted"
  final val EncryptedConfigField = "cipherConf"
  val awsClient = AWSKMSClientBuilder.standard().build()
  def getConfig(configPath: Option[String] = None): Config = {
    configPath match {
      case None => ConfigFactory.load()
      case Some(cp) => {
        val configFile = new File(cp)
        ConfigFactory.parseFile(configFile)
      }
    }
  }
  private[utils] def replaceConfigField(config: Config, configPath: String, configBlock: Config): Config = {
    Try(configBlock.withFallback(config)) match {
      case Success(config) => config
      case _ => throw new Exception(s"Error trying to replace '$configPath' configuration.")
    }
  }
  private[utils] def getConfigPlainText(config: Config, configPath: String): String = {
    Try(config.getObject(configPath).render(ConfigRenderOptions.concise())) match {
      case Success(plainText) => plainText
      case _ => throw new Exception(s"Path $configPath is not a valid configuration block.")
    }
  }
  def base64Encode(byteBuffer: ByteBuffer): String = {
    new String(Base64.getEncoder().encode(byteBuffer).array(), StandardCharsets.UTF_8)
  }
  def base64Decode(byteBuffer: ByteBuffer): String = {
    new String(Base64.getDecoder().decode(byteBuffer).array(), StandardCharsets.UTF_8)
  }
}
