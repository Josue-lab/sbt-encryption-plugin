package uk.co.telegraph.plugin.encryption

import cats.~>
import uk.co.telegraph.plugin.encryption.algebras.{EncryptionData, EncryptionState, Op, OpT}

package object tasks {
  trait BaseTask {
    def task(): OpT[EncryptionState, Unit]
    def interpreter: ~>[Op, EncryptionState]
    def runTask(): Unit = {
      val state = task.foldMap(interpreter)
      val initialState = EncryptionData(None, Seq(), "")
      state.run(initialState).value
    }
  }
}
