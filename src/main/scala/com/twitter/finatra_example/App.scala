package com.twitter.finatra_example

import com.twitter.finatra._
import com.twitter.finatra.ContentType._
// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp

object App extends FinatraServer {

  object ExampleApp {



    class Posts(tag: Tag) extends Table[(Int, String, String, Timestamp, Timestamp)](tag, "POSTS") {
      def id: Column[Int] = column[Int]("POST_ID") 
      def title: Column[String] = column[String]("POST_TITLE")
      def body: Column[String] = column[String]("BODY")
      def created_at: Column[Timestamp] = column[Timestamp]("CREATED_AT")
      def updated_at: Column[Timestamp] = column[Timestamp]("UPDATED_AT")
      // Every table needs a * projection with the same type as the table's type parameter
      def * = (id, title, body, created_at, updated_at)
    }
    val posts = TableQuery[Posts]

    val db = Database.forURL("jdbc:h2:file:/tmp/blogapp", driver = "org.h2.Driver") 

    def initDb() = {
      db.withSession { implicit session =>

      // Create the schema 
      (posts.ddl).create


      // Insert some posts
      posts += (101, "How I Met Your Mother", "Well it all started when...", Timestamp.valueOf("2013-01-01 18:48:05.123456"), Timestamp.valueOf("2013-01-01 18:48:05.123456"))
      posts += ( 49, "30 Rock", "Good God Lemon!", Timestamp.valueOf("2013-01-02 18:48:05.123456"), Timestamp.valueOf("2013-01-02 18:48:05.123456"))
      posts += (150, "Arrested Development", "The story of a wealthy family",  Timestamp.valueOf("2013-01-03 18:48:05.123456"), Timestamp.valueOf("2013-01-03 18:48:05.123456"))

}
}
}
  class ExampleApp extends Controller {
    import ExampleApp._

    //initDb()
    /* 

      class IndexView extends View {
        val template = "index.mustache"
      // posts.sortBy(_.created_at)

      val q1 = for {
        p <- posts
      } yield(p.id)

          val q2 = for {
      p <- posts
    } yield (p.title)

    val q3 = for {
      p <- posts
    } yield (p.created_at)

      val ids: List[Int] = q1.list
      val titless: List[String] = q2.list
      val created_ats: List[java.sql.Timestamp] = q3.list

 //      def printStuff() 
 //      return  "<li>
 //   <h4><a href="/posts/<%= post.id %>"><%= post.title %></a></h4>
 //   <p>Created: <%= post.created_at %></p>
 // </li>"



      // val title = sort(posts).title
      // val created_at = sort(posts).created_at
      }
*/
      get("/") { request =>
        val allPosts = db.withSession { implicit session => 
              val q1 = for {
        p <- posts
      } yield(p.id, p.title, p.created_at)

        q1.list

        }

         val indexView = new View {
          override val template = "index.mustache"
          import scala.collection.JavaConverters._
          // pass in map with key called posts 
//          val posts = allPosts.map { case (id, title, tstamp) => Map("id" -> id.toString, "title" -> title, "timestamp" -> tstamp.toString) }.asJava
val renderData = Map( "posts" ->   allPosts.map { case (id, title, tstamp) => Map("id" -> id.toString, "title" -> title, "timestamp" -> tstamp.toString) }.asJava)
          // val ids: List[Int] = ids0
          // val titles: List[String] = titles0
          // val created_ats: List[java.sql.Timestamp] = created_ats0

        }
      
        render.view(indexView).toFuture
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
