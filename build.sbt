lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.6.0-M2"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "es.cliq",
      scalaVersion    := "2.12.7"
    )),
    name := "cliqes",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe"       % "config"               % "1.3.4",
      "ch.qos.logback"     % "logback-classic"      % "1.2.3",
      "net.codingwell"    %% "scala-guice"          % "4.2.3",

      "com.geteventstore" %% "eventstore-client"    % "6.0.0",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test
    )
  )
/*
<dependency>
    <groupId>com.geteventstore</groupId>
    <artifactId>eventstore-client_2.12</artifactId>
    <version>5.0.8</version>
</dependency>

*/
