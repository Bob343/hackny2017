package tests

import services.ClarifaiService

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ClarifaiServiceSpec extends FlatSpec with Matchers {
  "The ClarifaiService" should "do some stuff" in {
    val cs = new ClarifaiService
    cs.main(new Array[String](0))
  }
}
