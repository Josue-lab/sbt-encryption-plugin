package uk.co.telegraph.plugin.encryption.tasks

import uk.co.telegraph.plugin.encryption.algebras.{EncryptionOps, EncryptionState, OpT}

trait EncryptTask {
  def task : OpT[EncryptionState, Unit] = {
    for {
      config <- EncryptionOps.getConfig(None)
      encryptedConfigPaths <- EncryptionOps.getEncryptedConfigPaths(config)
      encryptedConfig <- EncryptionOps.encrypt(???, encryptedConfigPaths, ???)
      _ <- EncryptionOps.writeConfig(encryptedConfig, ???)
    } yield ()
  }
}
