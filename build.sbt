name := "rmr"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.12.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" % Test,
  "ch.qos.logback" % "logback-classic" % "1.1.7" % Test,
  "org.jscala" %% "jscala-macros" % "0.5-SNAPSHOT",
  "org.mozilla" % "rhino" % "1.7.7.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)