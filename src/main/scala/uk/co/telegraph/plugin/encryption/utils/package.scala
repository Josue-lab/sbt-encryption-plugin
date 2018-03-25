package uk.co.telegraph.plugin.encryption

import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.typesafe.config._

import scala.collection.JavaConverters._
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
    Try {
      val configNoEncBlock = config.withValue(configPath, ConfigValueFactory.fromMap(Map.empty[String, String].asJava))
      configBlock.withFallback(configNoEncBlock)
    } match {
      case Success(config) => config
      case _ => throw new Exception(s"Error trying to replace '$configPath' configuration.")
    }
  }
  private[utils] def getConfigPlainText(config: Config, configPath: String): String = {
    Try {
      config.getAnyRef(configPath) match {
        case c: ConfigObject => {
          c.render(ConfigRenderOptions.concise())
        }
        case map: java.util.HashMap[String, _] => {
          ConfigFactory.parseMap(map).root().render(ConfigRenderOptions.concise())
        }
        case any: AnyRef => {
          any.toString
        }
      }
    } match {
      case Success(plainText) => plainText
      case _ => throw new Exception(s"Path $configPath is not a valid configuration block.")
    }
  }
  def base64Encode(byteBuffer: ByteBuffer): String = {
    new String(Base64.getEncoder().encode(byteBuffer).array(), StandardCharsets.UTF_8)
  }
  def base64Decode(byteBuffer: ByteBuffer): ByteBuffer = {
    Base64.getDecoder().decode(byteBuffer)
  }
}
