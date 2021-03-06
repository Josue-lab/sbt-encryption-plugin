package uk.co.telegraph.plugin.encryption

import sbt._

object EncryptionPlugin extends AutoPlugin
  with EncryptionKeys {

  override def trigger: PluginTrigger = allRequirements

  override lazy val projectSettings:Seq[Setting[_]] = baseSettings
}
