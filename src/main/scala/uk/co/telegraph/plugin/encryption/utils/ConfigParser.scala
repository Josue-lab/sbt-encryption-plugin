package uk.co.telegraph.plugin.encryption.utils

import scala.collection.JavaConverters._
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Success, Try}

trait ConfigParser {
  def encryptedConfiguration(config: Config): Seq[ConfigPath]
}

class DefaultConfigParser extends ConfigParser {
  override def encryptedConfiguration(config: Config): Seq[ConfigPath] = {
    config.entrySet()
      .asScala
      .filter(_.getKey.endsWith(s".$IsEncrypted"))
      .filter(kv => Try(config.getBoolean(kv.getKey)) match {
        case Success(isEncrypted) if isEncrypted => true
        case _ => false
      })
      .map(_.getKey().split('.').dropRight(1).mkString("."))
      .toList
  }
}

object ConfigParser {
  def apply(): ConfigParser = new DefaultConfigParser()
}
