package com.twitter.finatra_example

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra.FinatraServer
import com.twitter.finatra_example._

class AppSpec extends FlatSpecHelper {

  val app = new App.ExampleApp
  override val server = new FinatraServer
  server.register(app)

  
  "GET /notfound" should "respond 404" in {
    get("/notfound")
    response.body   should equal ("not found yo")
    response.code   should equal (404)
  }

  "GET /error" should "respond 500" in {
    get("/error")
    response.body   should equal ("whoops, divide by zero!")
    response.code   should equal (500)
  }

  "GET /hello" should "respond with hello world" in {
    get("/")
    response.body should equal ("hello world")
  }

  "GET /redirect" should "respond with /" in {
    get("/redirect")
    response.body should equal("Redirecting to <a href=\"http://localhost:7070/\">http://localhost:7070/</a>.")
    response.code should equal(301)
  }


  "GET /template" should "respond with a rendered template" in {
    get("/template")
    response.body should equal("Your value is random value here")
  }

}
