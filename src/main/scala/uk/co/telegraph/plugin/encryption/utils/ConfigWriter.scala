package uk.co.telegraph.plugin.encryption.utils

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.typesafe.config.Config

import scala.util.{Failure, Try}

object ConfigWriter {
  def writeToFile(config: Config, fileName: String): Unit ={
    val configText = config.root().render()
    Try(Files.write(Paths.get(fileName), configText.getBytes(StandardCharsets.UTF_8))) match {
      case Failure(_) => throw new Exception(s"Error trying to save into file $fileName \n $configText")
      case _ => Unit
    }
  }
}
