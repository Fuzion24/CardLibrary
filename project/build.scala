import sbt._
import Keys._

object AndroidBuild extends Build {
  lazy val main = Project(id = "main", base = file("."))
  lazy val tests = Project(id = "tests", base = file("tests"))
                   .dependsOn(main % "provided")
}
