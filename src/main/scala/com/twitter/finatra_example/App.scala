package com.twitter.finatra_example

import com.twitter.finatra._
import com.twitter.finatra.ContentType._
// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp

object App extends FinatraServer {

  class ExampleApp extends Controller {

    class Posts(tag: Tag) extends Table[(Int, String, String, Timestamp, Timestamp)](tag, "POSTS") {
      def id: Column[Int] = column[Int]("POST_ID") 
      def name: Column[String] = column[String]("POST_TITLE")
      def body: Column[String] = column[String]("BODY")
      def created_at: Column[Timestamp] = column[Timestamp]("CREATED_AT")
      def updated_at: Column[Timestamp] = column[Timestamp]("UPDATED_AT")
      // Every table needs a * projection with the same type as the table's type parameter
      def * = (id, name, body, created_at, updated_at)
    }
    val posts = TableQuery[Posts]

    Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
      implicit session =>

      // Create the schema 
      (posts.ddl).create

      class IndexView extends View {
        val posts = Post.order("created_at DESC")
        val title = "Welcome."
        val template = "index.mustache"
      }

      get("/") { request =>
        val IndexView = new IndexView
        render.view(IndexView).toFuture
      }

    }

    /**
     * Basic Example
     *
     * curl http://localhost:7070/ => "hello world"
     */

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

  }

  register(new ExampleApp())
}
