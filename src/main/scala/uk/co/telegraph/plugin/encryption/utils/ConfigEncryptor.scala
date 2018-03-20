package uk.co.telegraph.plugin.encryption.utils

import com.typesafe.config.Config

trait ConfigEncryptor {
  def encrypt(config: Config, configPath: String, key: String): Config

  def decrypt(config: Config, configPath: String, key: String): Config
}
