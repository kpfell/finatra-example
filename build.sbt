name := "finatra_example"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra" % "1.5.1",
  "com.h2database" % "h2" % "1.3.174"
)

resolvers +=
  "Twitter" at "http://maven.twttr.com"

seq(flywaySettings: _*)

flywayUrl := "jdbc:h2:file:target/foobar"

flywayUser := "SA"
