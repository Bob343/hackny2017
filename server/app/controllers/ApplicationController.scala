package controllers

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

@Singleton
class ApplicationController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def uploadForm = Action {
    Ok(views.html.form())
  }

  def upload = Action(parse.multipartFormData) { implicit request =>
    val formData = request.body.asFormUrlEncoded
    val lat = parseDouble(formData.get("lat"))
    val lon = parseDouble(formData.get("lon"))

    request.body.file("image").map { image =>
      val imageFile = image.ref.file
      Ok(s"${lat.toString},${lon.toString},${imageFile.toString}")
    }.get
  }

  def parseDouble(os: Option[Seq[String]]): Double =
    try {
      os.get.head.toDouble
    } catch {
      case _ : Throwable => 0.0
    }
}
