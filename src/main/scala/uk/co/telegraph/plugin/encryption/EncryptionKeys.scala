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
        val key = args(0)
        val configFile = args(1)
        val destination = args(2)
        println(
          s"""Key: $key
              |File: $configFile
              |Destination: $destination""".stripMargin)
        EncryptTask(key, configFile, destination, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      },
      decrypt := {
        val args: Seq[String] = spaceDelimited("").parsed
        val key = args(0)
        val configFile = args(1)
        val destination = args(2)
        DecryptTask(key, configFile, destination, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      }
    )
  }

  import autoImport._
  lazy val baseSettings: Seq[Setting[_]] = encryptionSettings
}
