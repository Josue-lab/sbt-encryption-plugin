package uk.co.telegraph.plugin.encryption

import sbt.Configuration
import sbt.Configurations._

trait EncryptionConfiguration {
  lazy val DeployStatic : Configuration = config("static" ) extend Runtime
  lazy val DeployDev    : Configuration = config("dev"    ) extend Runtime
  lazy val DeployCt     : Configuration = config("ct"     ) extend Runtime
  lazy val DeployQa     : Configuration = config("qa"     ) extend Runtime
  lazy val DeployPreProd: Configuration = config("preprod") extend Runtime
  lazy val DeployProd   : Configuration = config("prod"   ) extend Runtime
}

object EncryptionConfiguration extends EncryptionConfiguration