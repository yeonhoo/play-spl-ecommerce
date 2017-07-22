Common.appSettings(messagesFilesFrom = Seq("common"))

scalaVersion := "2.11.8"

lazy val ecommerce = (project in file(".")).enablePlugins(PlayScala).aggregate(common).dependsOn(common)

lazy val common = (project in file("modules/common")).enablePlugins(PlayScala)


 /*
  * Features
  */

lazy val features = (project in file("features/")).enablePlugins(PlayScala).aggregate(common).dependsOn(common).settings(
  scalaVersion := "2.11.8",
  name := "features",
  PlayKeys.devSettings ++=
    Seq(("play.http.router", "features.Routes"),
      ("db.default.driver", "org.h2.Driver"),
      ("db.default.url", "jdbc:h2:mem:play"),
      ("play.evolutions.enabled", "true"))
)

/*
 * Products
 */

// StaticHomePage, Product, Cart, Authentication, User
lazy val product1 = (project in file("prod/product1")).enablePlugins(PlayScala)
  .aggregate(features)
  .dependsOn(features).settings(
  scalaVersion := "2.11.8",
  name := "product1",
  PlayKeys.devSettings ++=
    Seq(("play.http.router", "product1.Routes"),
      ("db.default.driver", "org.h2.Driver"),
      ("db.default.url", "jdbc:h2:mem:play"),
      ("play.evolutions.enabled", "true"))
)

// DynamicHomePage, Product, Cart, Authentication, User
lazy val product2 = (project in file("prod/product2")).enablePlugins(PlayScala)
  .aggregate(features)
  .dependsOn(features).settings(
  scalaVersion := "2.11.8",
  name := "product1",
  PlayKeys.devSettings ++=
    Seq(("play.http.router", "product2.Routes"),
      ("db.default.driver", "org.h2.Driver"),
      ("db.default.url", "jdbc:h2:mem:play"),
      ("play.evolutions.enabled", "true"))
)

// StaticHomePage, Product
// testando
lazy val product3 = (project in file("prod/product3")).enablePlugins(PlayScala)
  .aggregate(features)
  .dependsOn(features).settings(
  scalaVersion := "2.11.8",
  name := "product3",
  PlayKeys.devSettings ++=
    Seq(("play.http.router", "product3.Routes"),
      ("db.default.driver", "org.h2.Driver"),
      ("db.default.url", "jdbc:h2:mem:play"),
      ("play.evolutions.enabled", "true"))
)




libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  