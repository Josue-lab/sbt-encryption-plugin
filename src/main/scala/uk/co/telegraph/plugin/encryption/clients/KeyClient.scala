package uk.co.telegraph.plugin.encryption.clients

trait KeyClient {
  def getKey(): String
}
