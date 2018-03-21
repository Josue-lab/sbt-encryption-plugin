package uk.co.telegraph.plugin.encryption.algebras.aws

import cats.data.State
import cats.~>
import com.typesafe.config.Config
import sbt.Logger
import uk.co.telegraph.plugin.encryption.algebras._
import uk.co.telegraph.plugin.encryption.utils.aws.KMSConfigEncryptor
import uk.co.telegraph.plugin.encryption.utils.{ConfigParser, ConfigPath, ConfigWriter}
import uk.co.telegraph.plugin.encryption.utils._

object KMSInterpreter extends Interpreter {
  override def interpreter(key: String, config: Config)(implicit log: Logger): ~>[Op, EncryptionState] = {
    new (Op ~> EncryptionState) {
      def apply[A](op : Op[A]) : EncryptionState[A] = {
        op match {
          case GetConfig(configPath) => {
            State { state =>
              val config = getConfig(configPath)
              (state.copy(config = config), config.asInstanceOf[A])
            }
          }
          case GetConfigPaths(config: Config) =>
            State { state =>
              val configPaths = ConfigParser().encryptedConfiguration(config)
              log.info(s"Config paths to encrypt: ${configPaths.mkString("\n","\n","\n")} ")
              (state.copy(configPaths = configPaths), configPaths.asInstanceOf[A])
            }
          case WriteConfig(config: Config, configFile: String) =>
            State { state =>
              ConfigWriter.writeToFile(config, configFile)
              log.info(s"Writting config to '$configFile' ")
              (state, ().asInstanceOf[A])
            }
          case Encrypt(config: Config, configPaths: Seq[ConfigPath], key: String) =>
            State { state =>
              val encryptedConfig = configPaths.map(configPath => {
                log.info(s"Encrypting '$configPath'")
                KMSConfigEncryptor.encrypt(config, configPath, key)
              }).last
              (state.copy(config = encryptedConfig), config.asInstanceOf[A])
            }
          case Decrypt(config: Config, configPaths: Seq[ConfigPath], key: String) =>
            State { state =>
              val decryptedConfig = configPaths.map(configPath => {
                log.info(s"Decrypting '$configPath'")
                KMSConfigEncryptor.decrypt(config, configPath, key)
              }).last
              (state.copy(config = decryptedConfig), config.asInstanceOf[A])
            }
        }
      }
    }
  }
}
