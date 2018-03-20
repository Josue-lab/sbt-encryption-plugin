package uk.co.telegraph.plugin.encryption

import sbt._
import sbt.Keys._

trait EncryptionKeys {

  lazy val configFile: SettingKey[String] = SettingKey[String] ("config-file", "Config file to encryp/decrypt.")
  lazy val encryptKey: SettingKey[String] = SettingKey[String] ("encrypt-key", "Config file to encryp/decrypt.")

  lazy val encrypt : TaskKey[URI] = TaskKey[URI] ("encrypt", "Task used to encrypt a configuration file.")
  lazy val decrypt : TaskKey[URI] = TaskKey[URI] ("decrypt", "Task used to decrypt a configuration file.")

  lazy val baseStackSettings: Seq[Setting[_]] = Seq(
  )
}
