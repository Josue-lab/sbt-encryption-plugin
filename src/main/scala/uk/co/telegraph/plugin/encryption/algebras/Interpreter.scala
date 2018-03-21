package uk.co.telegraph.plugin.encryption.algebras

import cats.~>
import sbt.Logger

trait Interpreter {
  def interpreter()(implicit log:Logger): ~>[Op, EncryptionState]
}
