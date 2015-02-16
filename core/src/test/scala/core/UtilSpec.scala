package core

import org.scalatest.{FunSpec, Matchers}

class UtilSpec extends FunSpec with Matchers {

  lazy val service = TestHelper.parseFile(s"api/api.json").service.get
  private lazy val visibilityEnum = service.enums.find(_.name == "visibility").getOrElse {
    sys.error("No visibility enum found")
  }

  it("namedParametersInPath") {
    Util.namedParametersInPath("/users") should be(Seq.empty)
    Util.namedParametersInPath("/users/:guid") should be(Seq("guid"))
    Util.namedParametersInPath("/:org/foo/:version") should be(Seq("org", "version"))
    Util.namedParametersInPath("/:org/:service/:version") should be(Seq("org", "service", "version"))
  }

  it("isValidEnumValue") {
    Util.isValidEnumValue(visibilityEnum, "user") should be(true)
    Util.isValidEnumValue(visibilityEnum, "organization") should be(true)
    Util.isValidEnumValue(visibilityEnum, "foobar") should be(false)
  }

  it("isValidUri") {
    Seq(
      "http://www.apidoc.me",
      "https://www.apidoc.me",
      "  http://www.apidoc.me   ",
      " HTTPS://WWW.APIDOC.ME  ",
      "http://apidoc.me",
      "https://api.apidoc.me",
      "file:///tmp/foo.json"
    ).foreach { uri =>
      Util.isValidUri(uri) should be(true)
    }

    Seq(
      "www.apidoc.me",
      "apidoc",
      "bar baz"
    ).foreach { uri =>
      Util.isValidUri(uri) should be(false)
    }
  }

}
