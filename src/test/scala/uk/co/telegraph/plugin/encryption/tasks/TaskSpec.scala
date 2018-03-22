package uk.co.telegraph.plugin.encryption.tasks

import java.io.File

import com.typesafe.config.ConfigFactory
import sbt.ConsoleLogger

trait TaskSpec {
  val config = ConfigFactory.load()
  val kmsKey = config.getString("kmsKey")
  def getResourceFileAbsolutePath(fileName: String) = new File(s"src/test/resources/$fileName").getAbsolutePath
  val logger = ConsoleLogger()
}
