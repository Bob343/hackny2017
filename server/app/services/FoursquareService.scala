package services

import java.util.ArrayList
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

  /** Retrieves a list of Venues within 100 meters of (lat, lon) from the
   *  Foursquare API, and extracts the Venue IDs and Venue names from the
   *  JSON response
   */
  def findVenues(lat: Double, lon: Double): List[(String,String)] = {
    val url = "https://api.foursquare.com/v2/venues/search"
    val foursquareResponse =
      Http(url).param("client_id", CLIENT_ID)
         .param("client_secret", CLIENT_SECRET)
         .param("v", "20130815")
         .param("ll", s"${lat.toString},${lon.toString}")
         .param("radius", "100")
         .param("intent", "browse")
         .asString

      JsonService.extractIdsAndNames(foursquareResponse.body)
  }

  /** Reformats List of Tuples with Photo URLs and Venue name
   *  to a Java ArrayList with the first index as the name and
   *  subsequent items as the URLs
   */
  def venuePhotosAsArrayList(venuePhotos: List[(List[String], String)]): ArrayList[ArrayList[String]] = {
    val arrPhotos = new ArrayList[ArrayList[String]]()
    venuePhotos.foreach { tuple =>
      val arr = new ArrayList[String]()
      arr.add(tuple._2)
      tuple._1.foreach { url =>
        arr.add(url)
      }
      arrPhotos.add(arr)
    }
    return arrPhotos
  }

  /** Maps a list of venues in the form (Venue ID, Venue Name) to a list of
   *  the form (Venue Photo URLs, Venue Name) where the Venue Photo URLs
   *  are in a List of Strings
   */
  def venuePhotos(venueList: List[(String,String)]): List[(List[String], String)] =
    venueList.map { x =>
      (getPhotos(x._1), x._2)
    }.filter { x =>
      !x._1.isEmpty
    }

  def getPhotos(venueId: String): List[String] = {
    val url = s"https://api.foursquare.com/v2/venues/${venueId}/photos"
    val foursquareResponse =
      Http(url).param("client_id", CLIENT_ID)
         .param("client_secret", CLIENT_SECRET)
         .param("v", "20130815")
         .asString

    JsonService.extractPhotoUrls(foursquareResponse.body)
  }
}
