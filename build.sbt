// Add the SBT-Android plugin
androidDefaults

// Set the project name
name := "CardLibrary"

// Set the project version
version := "0.1"

// Set the project version code
versionCode := 0

// Set the version of Scala to use
scalaVersion := "2.10.2"

// Set the Android platform name
platformName := "android-16"

// Make the Java compiler work better with Android apps
javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6")

// Set the key alias
keyalias := "change-me"

// Add ScalaTest
libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
