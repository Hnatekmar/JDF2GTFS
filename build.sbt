name := "jdf2gtfs"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.195",
  "org.scalikejdbc" %% "scalikejdbc" % "3.0.0",
  "com.github.tototoshi" %% "scala-csv" % "1.3.4",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3")
