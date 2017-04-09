package controllers

import services.FoursquareService

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject()(fs: FoursquareService) extends Controller {

  def index = Action {
    val venues = fs.findVenues(40.728736,-73.995337)
    val photos = fs.venuePhotos(venues)
    Ok(views.html.index(photos.toString))
  }
}
