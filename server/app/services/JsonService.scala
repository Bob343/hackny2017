package services

import com.google.gson._
import scala.collection.JavaConverters._

object JsonService {

  val parser = new JsonParser()

  // Venue parsing functions

  def extractIdsAndNames(str: String) =
    getIdsAndNames(extractVenues(str))

  def extractVenues(str: String): List[JsonObject] =
    jsonArrToList(
      getResponse(str).getAsJsonArray("venues")
    )

  def getIdsAndNames(jsonSeq: List[JsonObject]): List[(String,String)] =
    jsonSeq.map { venue =>
      val id = venue.get("id").getAsString
      val name = venue.get("name").getAsString
      (id, name)
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
