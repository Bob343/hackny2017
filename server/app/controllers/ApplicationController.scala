package controllers

import services.ClarifaiService
import services.FoursquareService

import java.io.File
import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

@Singleton
class ApplicationController @Inject()(val messagesApi: MessagesApi,
    fs: FoursquareService, cs: ClarifaiService) extends Controller with I18nSupport {

  def uploadForm = Action {
    Ok(views.html.form())
  }

  def upload = Action(parse.multipartFormData) { implicit request =>
    val formData = request.body.asFormUrlEncoded
    val lat = parseDouble(formData.get("lat"))
    val lon = parseDouble(formData.get("lon"))
    val trainingPhotos = fs.venuePhotos(lat, lon)
    println(trainingPhotos)
    println(trainingPhotos.size)

    request.body.file("image").map { image =>
      val imageFile = image.ref.file
      cs.run(trainingPhotos, imageFile)
      Ok("Stuff happened")
    }.get
  }

  def parseDouble(os: Option[Seq[String]]): Double =
    try {
      os.get.head.toDouble
    } catch {
      case _ : Throwable => 0.0
    }
}