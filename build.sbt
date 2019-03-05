name := "ChemondisChallenge"
 
version := "1.0" 
      
lazy val `chemondischallenge` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.8"

libraryDependencies ++= Seq("com.typesafe.play" %% "play-json" % "2.6.9", jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      