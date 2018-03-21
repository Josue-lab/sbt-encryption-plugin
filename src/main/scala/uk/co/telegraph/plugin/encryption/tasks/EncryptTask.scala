package uk.co.telegraph.plugin.encryption.tasks

import cats.~>
import uk.co.telegraph.plugin.encryption.algebras.{EncryptionOps, EncryptionState, Op, OpT}

class EncryptTask(
  key: String,
  configFile: String,
  destination: String,
  val interpreter: ~>[Op, EncryptionState]
) extends BaseTask {
  def task : OpT[EncryptionState, Unit] = {
    for {
      config <- EncryptionOps.getConfig(Some(configFile))
      encryptedConfigPaths <- EncryptionOps.getEncryptedConfigPaths(config)
      encryptedConfig <- EncryptionOps.encrypt(config, encryptedConfigPaths, key)
      _ <- EncryptionOps.writeConfig(encryptedConfig, configFile)
    } yield ()
  }
}

object EncryptTask {
  def apply(key: String, configFile: String, destination: String, interpreter: ~>[Op, EncryptionState]) =
    new EncryptTask(key, configFile, destination, interpreter)
}
