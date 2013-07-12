import sbt._
import Keys._
import sbtandroid.AndroidPlugin
import sbtandroid.AndroidPlugin._

object AndroidBuild extends Build {
  lazy val main = Project(id = "main", base = file("."), settings = mainSettings)
  
  lazy val tests = Project(id = "tests", base = file("tests"))
                   .dependsOn(main % "provided")

  lazy val mainSettings = Defaults.defaultSettings ++ AndroidPlugin.androidDefaults ++ Seq(
  	  name := "CardLibrary",
      version := "0.1",
      versionCode := 0,
      scalaVersion := "2.10.2",
      platformName := "android-16",
  	  libraryDependencies ++= Seq("org.scaloid" %% "scaloid" % "2.1-8",
  	  							  "org.scalatest" %% "scalatest" % "1.9.1" % "test"),
	    javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6"),
      keyalias := "change-me"
    )
}
