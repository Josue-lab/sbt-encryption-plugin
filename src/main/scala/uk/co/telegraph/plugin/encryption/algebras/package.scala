package uk.co.telegraph.plugin.encryption

import cats.data.State
import cats.free.FreeT
import com.typesafe.config.Config
import uk.co.telegraph.plugin.encryption.utils.ConfigPath

package object algebras {
  sealed trait Op[A]
  final case class GetConfig(configPath: Option[String]) extends Op[Config]
  final case class GetConfigPaths(config: Config) extends Op[Seq[ConfigPath]]
  final case class WriteConfig(config: Config, configFile: String) extends Op[Unit]
  final case class Encrypt(config: Config, configPaths: Seq[ConfigPath], key: String) extends Op[Config]
  final case class Decrypt(config: Config, configPath: Seq[ConfigPath], key: String) extends Op[Config]

  final case class EncryptionData(config: Option[Config], configPaths: Seq[ConfigPath], key: String)
  type EncryptionState[A] = State[EncryptionData, A]
  type OpT[M[_], A] = FreeT[Op, M, A]

  object EncryptionOps {
    def getConfig(configPath : Option[String]) : OpT[EncryptionState, Config] =
      FreeT.liftF[Op, EncryptionState, Config](GetConfig(configPath))
    def getEncryptedConfigPaths(config : Config) : OpT[EncryptionState, Seq[ConfigPath]] =
      FreeT.liftF[Op, EncryptionState, Seq[ConfigPath]](GetConfigPaths(config))
    def writeConfig(config: Config, configFile: String) : OpT[EncryptionState, Unit] =
      FreeT.liftF[Op, EncryptionState, Unit](WriteConfig(config, configFile))
    def encrypt(config: Config, configPaths: Seq[ConfigPath], key: String) : OpT[EncryptionState, Config] =
      FreeT.liftF[Op, EncryptionState, Config](Encrypt(config, configPaths, key))
    def decrypt(config: Config, configPaths: Seq[ConfigPath], key: String) : OpT[EncryptionState, Config] =
      FreeT.liftF[Op, EncryptionState, Config](Decrypt(config, configPaths, key))
  }
}
