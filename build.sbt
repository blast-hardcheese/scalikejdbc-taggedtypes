name := "typesafety"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"                       % "2.2.0",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin"           % "2.3.4",
  "com.h2database"  %  "h2"                                % "1.4.182",
  "ch.qos.logback"  %  "logback-classic"                   % "1.1.2",
  cache
)

play.Project.playScalaSettings
