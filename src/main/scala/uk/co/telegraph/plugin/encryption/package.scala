package uk.co.telegraph.plugin

import com.typesafe.config.ConfigFactory

package object encryption {
  val config = ConfigFactory.load()
}
