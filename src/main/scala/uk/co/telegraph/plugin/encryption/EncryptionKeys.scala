package uk.co.telegraph.plugin.encryption

import sbt.Keys.streams
import sbt._
import uk.co.telegraph.plugin.encryption.algebras.aws.KMSInterpreter
import uk.co.telegraph.plugin.encryption.tasks.{DecryptTask, EncryptTask}
import sbt.complete.Parsers.spaceDelimited

trait EncryptionKeys {
  lazy val encrypt = inputKey[Unit]("Task used to encrypt a configuration file.")
  lazy val decrypt = inputKey[Unit]("Task used to decrypt a configuration file.")

  object autoImport {
    lazy val encryptionSettings: Seq[Setting[_]] = Seq(
      encrypt := {
        val Seq(key, configFile, destination) = spaceDelimited("").parsed
        EncryptTask(key, configFile, destination, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      },
      decrypt := {
        val Seq(configFile, destination) = spaceDelimited("").parsed
        DecryptTask(configFile, destination, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      }
    )
  }

  import autoImport._
  lazy val baseSettings: Seq[Setting[_]] = encryptionSettings
}
