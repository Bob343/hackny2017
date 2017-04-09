package controllers

import services.ClarifaiService
import services.FoursquareService

import akka.actor.ActorSystem
import java.io.File
import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

@Singleton
class ApplicationController @Inject()(val messagesApi: MessagesApi,
    fs: FoursquareService, cs: ClarifaiService, actorSystem: ActorSystem)
    (implicit exec: ExecutionContext) extends Controller with I18nSupport {

  def uploadForm = Action {
    Ok(views.html.form())
  }

  def upload = Action.async(parse.multipartFormData) { implicit request =>
    val formData = request.body.asFormUrlEncoded
    val lat = parseDouble(formData.get("lat"))
    val lon = parseDouble(formData.get("lon"))

    request.body.file("image").map {
      image => getFuturePrediction(lat,lon,image.ref.file).map {
        prediction => Ok(prediction)
      }
    }.get
  }

  private def getFuturePrediction(lat: Double, lon: Double, image: File): Future[String] = {
    Future {
      val trainingPhotos = fs.venuePhotos(lat, lon)
      cs.run(trainingPhotos, image)
    }
  }

  def parseDouble(os: Option[Seq[String]]): Double =
    try {
      os.get.head.toDouble
    } catch {
      case _ : Throwable => 0.0
    }
}
