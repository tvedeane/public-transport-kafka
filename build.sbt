name := "public-transport-kafka"

version := "0.1"

scalaVersion := "2.13.3"
val scalatraVersion = "2.7.0"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "2.5.0",
  "org.scalatra" %% "scalatra" % scalatraVersion,
  "org.scalatra" %% "scalatra-json" % scalatraVersion,
  "org.json4s" %% "json4s-jackson" % "3.6.9",
  "javax.servlet" % "javax.servlet-api" % "4.0.1" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.29.v20200521" % "compile;container",
  "org.scalatra" %% "scalatra-scalatest" % scalatraVersion % "test",
  "org.scalatestplus" %% "scalatestplus-mockito" % "1.0.0-M2" % Test
)

enablePlugins(ScalatraPlugin)
containerPort in Jetty := 8081