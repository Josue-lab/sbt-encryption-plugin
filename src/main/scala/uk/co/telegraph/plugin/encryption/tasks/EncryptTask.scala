package uk.co.telegraph.plugin.encryption.tasks

import uk.co.telegraph.plugin.encryption.algebras.{EncryptionOps, EncryptionState, OpT}

trait EncryptTask extends BaseTask {
  val key = "asdf"
  val destination = "application.conf"
  def task : OpT[EncryptionState, Unit] = {
    for {
      config <- EncryptionOps.getConfig(None)
      encryptedConfigPaths <- EncryptionOps.getEncryptedConfigPaths(config)
      encryptedConfig <- EncryptionOps.encrypt(config, encryptedConfigPaths, key)
      _ <- EncryptionOps.writeConfig(encryptedConfig, destination)
    } yield ()
  }
}
