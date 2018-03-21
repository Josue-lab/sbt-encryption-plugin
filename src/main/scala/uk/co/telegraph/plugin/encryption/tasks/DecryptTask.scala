package uk.co.telegraph.plugin.encryption.tasks

import uk.co.telegraph.plugin.encryption.algebras.{EncryptionOps, EncryptionState, OpT}

trait DecryptTask {
  def task : OpT[EncryptionState, Unit] = {
    for {
      config <- EncryptionOps.getConfig(None)
      encryptedConfigPaths <- EncryptionOps.getEncryptedConfigPaths(config)
      decryptedConfig <- EncryptionOps.decrypt(???, encryptedConfigPaths, ???)
      _ <- EncryptionOps.writeConfig(decryptedConfig, ???)
    } yield ()
  }
}
