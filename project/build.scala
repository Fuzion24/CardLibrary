import sbt._
import Keys._

object AndroidBuild extends Build {
  lazy val main = Project(id = "main", base = file("."), settings = mainSettings)
  lazy val tests = Project(id = "tests", base = file("tests"))
                   .dependsOn(main % "provided")
  lazy val mainSettings = Defaults.defaultSettings ++ Seq(
  	  libraryDependencies ++= Seq("org.scaloid" %% "scaloid" % "2.1-8")  
  	)
}
