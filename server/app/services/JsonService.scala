package services

import models.SitePrediction

import com.google.gson._
import java.util.ArrayList
import scala.collection.JavaConverters._

object JsonService {

  val parser = new JsonParser()

  // Venue parsing functions

  def extractVals(str: String): List[(String,String,String)] =
    extractVals(extractVenues(str))

  def extractVenues(str: String): List[JsonObject] =
    jsonArrToList(
      getResponse(str).getAsJsonArray("venues")
    )

  def extractVals(jsonSeq: List[JsonObject]): List[(String,String,String)] =
    jsonSeq.map { venue =>
      val id = venue.get("id").getAsString
      val name = venue.get("name").getAsString

      try {
        val addr = venue.get("location").getAsJsonObject.get("address").getAsString
        (id, name, addr)
      } catch {
        case _ : Throwable => (id, name, "")
      }

    }

  // Photo parsing functions

  def extractPhotoUrls(str: String): List[String] =
    getPhotoUrls(getPhotoItems(getResponse(str)))

  def getPhotoUrls(photoItems: JsonArray): List[String] =
    jsonArrToList(photoItems).map { photoItem =>
      val prefix = photoItem.get("prefix").getAsString
      val width = photoItem.get("width").getAsString
      val height = photoItem.get("height").getAsString
      val suffix = photoItem.get("suffix").getAsString

      s"${prefix}${width}x${height}${suffix}"
    }

  def getPhotoItems(response: JsonObject): JsonArray =
    response.get("photos")
            .getAsJsonObject
            .getAsJsonArray("items")

  // Prediction serialization functions

  def serializePredictions(predictions: ArrayList[ArrayList[String]]): String = {
    val sb = new StringBuilder("{\"sites\": [")
    predictions.asScala.foreach { prediction =>
      sb.append(serializePrediction(prediction))
      sb.append(",")
    }

    s"""${sb.substring(0, sb.size - 1)}]}"""
  }

  def serializePrediction(vals: ArrayList[String]): String = {
    val pred = SitePrediction(vals.get(0), vals.get(1), vals.get(2), vals.get(3))
    pred.toJson
  }

  // Helper functions

  def getResponse(str: String): JsonObject =
    parser.parse(str)
          .getAsJsonObject
          .get("response")
          .getAsJsonObject

  def jsonArrToList(jArr: JsonArray): List[JsonObject] =
    jArr.iterator
        .asScala
        .toList
        .map(_.getAsJsonObject)
}
