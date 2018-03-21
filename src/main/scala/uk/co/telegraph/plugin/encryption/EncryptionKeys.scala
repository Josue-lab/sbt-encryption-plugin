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
    def getArgs() = {
      val args = spaceDelimited("<arg>").parsed
      (args(0),args(1))
    }
    lazy val encryptionSettings: Seq[Setting[_]] = Seq(
      encrypt := {
        val (key, configFile) = getArgs
        EncryptTask(key, configFile, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      },
      decrypt := {
        val (key, configFile) = getArgs
        DecryptTask(key, configFile, KMSInterpreter.interpreter()(streams.value.log)).runTask()
      }
    )
  }
//
//  val greeting = inputKey[Unit]("A demo input task.")
//
//  greeting := {
//    val args: Seq[String] = spaceDelimited("<arg>").parsed
//    args foreach println
//  }

  import autoImport._
  lazy val baseSettings: Seq[Setting[_]] = encryptionSettings
}
