package uk.co.telegraph.plugin.encryption.utils

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

import com.typesafe.config.Config

import scala.util.{Failure, Try}

object ConfigWriter {
  private val openOption = Seq(StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)
  def writeToFile(config: Config, fileName: String): Unit ={
    val configText = config.root().render()
    Try{
      if(Files.notExists(Paths.get(fileName)))
        Files.createFile(Paths.get(fileName))
      Files.write(Paths.get(fileName), configText.getBytes(StandardCharsets.UTF_8), openOption:_*)
    } match {
      case Failure(_) => throw new Exception(s"Error trying to save into file $fileName \n $configText")
      case _ => Unit
    }
  }
}
