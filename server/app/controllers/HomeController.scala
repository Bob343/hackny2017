package controllers

import services.FoursquareService

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject()(fs: FoursquareService) extends Controller {

  def index = Action {
    Ok(views.html.index(fs.debug))
  }
}
