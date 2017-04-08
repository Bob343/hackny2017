package services

import javax.inject.Inject
import javax.inject.Singleton
import play.api.Configuration
import scalaj.http._

@Singleton
class FoursquareService @Inject()(config: Configuration) {
  lazy val CLIENT_ID = config.getString("foursquare.api.client_id").get
  lazy val CLIENT_SECRET = config.getString("foursquare.api.client_secret").get

  def debug =
    s"Client ID: ${CLIENT_ID}, Client Secret: ${CLIENT_SECRET}"

  def findVenues(lat: Double, lon: Double) = {
    val url = "https://api.foursquare.com/v2/venues/search"
    val foursquareResponse =
      Http(url).param("client_id", CLIENT_ID)
         .param("client_secret", CLIENT_SECRET)
         .param("v", "20130815")
         .param("ll", s"${lat.toString},${lon.toString}")
         .param("radius", "100")
         .param("intent", "browse")
         //.asString
         .execute(parser = {inputstream =>
           JsonService.parse[Map[String,String]](inputstream)
         })

    foursquareResponse.toString
  }
}
