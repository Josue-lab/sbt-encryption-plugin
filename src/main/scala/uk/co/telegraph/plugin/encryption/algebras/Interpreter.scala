package uk.co.telegraph.plugin.encryption.algebras

import cats.~>
import com.typesafe.config.Config
import sbt.Logger

trait Interpreter {
  def interpreter(key: String, config: Config)(implicit log:Logger): ~>[Op, EncryptionState]
}
