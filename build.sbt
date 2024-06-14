name := "DatadisAnalyzer"

version := "0.1.0"

scalaVersion := "2.13.8"
enablePlugins(PackPlugin)

val log4jVersion = "2.23.1"
val sf4jVersion = "2.0.13"
val typesafeConfigVersion = "1.4.3"
val akkaVersion = "2.7.0"
val akkaHttpVersion = "10.5.2"
val influxdbVersion = "6.12.0"
val scalaTestVersion = "3.2.17"
val easymockVersion = "3.2.17.0"
val wiremockVersion = "3.0.1"
val commonsLangVersion = "3.14.0"

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
  "org.slf4j" % "slf4j-log4j12" % sf4jVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.influxdb" %% "influxdb-client-scala" % influxdbVersion,
  "org.apache.commons" % "commons-lang3" % commonsLangVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalatestplus" %% "easymock-5-1" % easymockVersion % Test,
  "com.github.tomakehurst" % "wiremock-jre8" % wiremockVersion % Test
)
