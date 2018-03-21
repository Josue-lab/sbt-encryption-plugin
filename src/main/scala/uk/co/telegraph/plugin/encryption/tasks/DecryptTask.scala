package uk.co.telegraph.plugin.encryption.tasks

import cats.~>
import uk.co.telegraph.plugin.encryption.algebras.{EncryptionOps, EncryptionState, Op, OpT}

class DecryptTask(
  key: String,
  configFile: String,
  destination: String,
  val interpreter: ~>[Op, EncryptionState]
) extends BaseTask {
  def task : OpT[EncryptionState, Unit] = {
    for {
      config <- EncryptionOps.getConfig(Some(configFile))
      encryptedConfigPaths <- EncryptionOps.getEncryptedConfigPaths(config)
      decryptedConfig <- EncryptionOps.decrypt(config, encryptedConfigPaths, key)
      _ <- EncryptionOps.writeConfig(decryptedConfig, destination)
    } yield ()
  }
}

object DecryptTask {
  def apply(key: String, configFile: String, destination: String, interpreter: ~>[Op, EncryptionState]) =
    new DecryptTask(key, configFile, destination, interpreter)
}