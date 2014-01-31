package com.twitter.finatra_example

import com.twitter.finatra._
import com.twitter.finatra.ContentType._

object App extends FinatraServer {

  
  class ExampleApp extends Controller {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/ => "hello world"
     */

    class AnView extends View {
      val template = "index.mustache"
    }

    get("/") { request =>
      val anView = new AnView
      render.view(anView).toFuture
    }

    delete("/photos") { request =>
      render.plain("deleted!").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") { request =>
      val username = request.routeParams.getOrElse("username", "default_user")
      render.plain("hello " + username).toFuture
    }

    /**
     * Redirects
     *
     * curl http://localhost:7070/redirect
     */
    get("/redirect") { request =>
      redirect("http://localhost:7070/", permanent = true).toFuture
    }

    /**
     * Rendering views
     *
     * curl http://localhost:7070/template
     */
    class AnView extends View {
      val template = "an_view.mustache"
      val some_val = "random value here"
    }

    get("/template") { request =>
      val anView = new AnView
      render.view(anView).toFuture
    }

  }

  register(new ExampleApp())
}
