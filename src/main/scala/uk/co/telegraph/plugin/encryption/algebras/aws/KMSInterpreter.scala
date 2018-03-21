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
  override def interpreter()(implicit log: Logger): ~>[Op, EncryptionState] = {
    new (Op ~> EncryptionState) {
      def apply[A](op : Op[A]) : EncryptionState[A] = {
        op match {
          case GetConfig(configPath) => {
            State { state =>
              log.info(s"Getting config file from $configPath.")
              val config = getConfig(configPath)
              (state.copy(config = Some(config)), config.asInstanceOf[A])
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
              log.info(s"Writting config to '$configFile' ")
              ConfigWriter.writeToFile(config, configFile)
              (state, ().asInstanceOf[A])
            }
          case Encrypt(config: Config, configPaths: Seq[ConfigPath], key: String) =>
            State { state =>
              val encryptedConfig = configPaths.map(configPath => {
                log.info(s"Encrypting '$configPath'")
                KMSConfigEncryptor.encrypt(config, configPath, key)
              }).last
              (state.copy(config = Some(encryptedConfig)), config.asInstanceOf[A])
            }
          case Decrypt(config: Config, configPaths: Seq[ConfigPath], key: String) =>
            State { state =>
              val decryptedConfig = configPaths.map(configPath => {
                log.info(s"Decrypting '$configPath'")
                KMSConfigEncryptor.decrypt(config, configPath, key)
              }).last
              (state.copy(config = Some(decryptedConfig)), config.asInstanceOf[A])
            }
        }
      }
    }
  }
}
