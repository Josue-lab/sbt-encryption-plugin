package uk.co.telegraph.plugin.encryption

import sbt._
import sbt.Keys._

trait EncryptionKeys {

  lazy val configFile: SettingKey[String]    = SettingKey[String]    ("config-file", "Config file to encryp/decrypt.")

  lazy val encrypt : TaskKey[URI]            = TaskKey[URI]          ("encrypt",     "Task used to encrypt a configuration file.")

  lazy val baseStackSettings: Seq[Setting[_]] = Seq(
  )
}
