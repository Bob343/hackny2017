package models

case class SitePrediction(name: String, address: String, imgUrl: String, confidence: String) {
  def toJson(): String =
    s"""{"site": {
          "name": "${this.name}",
          "address": "${this.address}",
          "image": "${this.imgUrl}",
          "confidence": "${this.confidence}"
        }}"""
}
