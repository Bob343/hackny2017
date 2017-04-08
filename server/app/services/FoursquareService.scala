package services

import javax.inject.Inject
import javax.inject.Singleton

import play.api.Configuration

@Singleton
class FoursquareService @Inject()(config: Configuration) {
  lazy val CLIENT_ID = config.getString("foursquare.api.client_id")
  lazy val CLIENT_SECRET = config.getString("foursquare.api.client_secret")

  def debug =
    s"Client ID: ${CLIENT_ID}, Client Secret: ${CLIENT_SECRET}"
}
