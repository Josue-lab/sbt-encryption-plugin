package uk.co.telegraph.plugin.encryption

import sbt.Keys.streams
import sbt._
import uk.co.telegraph.plugin.encryption.algebras.aws.KMSInterpreter
import uk.co.telegraph.plugin.encryption.tasks.{DecryptTask, EncryptTask}
import sbt.complete.Parsers.spaceDelimited

trait EncryptionKeys {
  lazy val encrypt = inputKey[Unit]("Task used to encrypt a configuration file.")
  lazy val decrypt = inputKey[Unit]("Task used to decrypt a configuration file.")

//  val key = "e11fd199-bf05-4dd7-b018-a3b6be63d03f"
//  val destination = "application.conf"
  object autoImport {
    def getArgs(args: Seq[String]) = {
      (args(0),args(1))
    }
    lazy val encryptionSettings: Seq[Setting[_]] = Seq(
      encrypt := {
        val args: Seq[String] = spaceDelimited("").parsed
        println(s"Args: $args")
        EncryptTask(args(0), args(1), KMSInterpreter.interpreter()(streams.value.log)).runTask()
      },
      decrypt := {
        val (key, configFile) = getArgs(spaceDelimited("").parsed)
        DecryptTask(key, configFile, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      }
    )
  }

  import autoImport._
  lazy val baseSettings: Seq[Setting[_]] = encryptionSettings
}
