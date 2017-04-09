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

  def upload = Action { implicit request =>//(parse.multipartFormData) { implicit request =>
    /*val formData = request.body.asFormUrlEncoded
    val lat = parseDouble(formData.get("lat"))
    val lon = parseDouble(formData.get("lon"))
    val trainingPhotos = fs.venuePhotos(lat, lon)

    println(request.body)

    request.body.file("image").map { image =>
      val imageFile = image.ref.file
      val prediction = cs.run(trainingPhotos, imageFile)
      Ok(prediction)
    }.get*/
    Ok(request.body.toString)
  }

  def parseDouble(os: Option[Seq[String]]): Double =
    try {
      os.get.head.toDouble
    } catch {
      case _ : Throwable => 0.0
    }
}
