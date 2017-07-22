Common.moduleSettings("common")

// Add here the specific settings for this module

libraryDependencies ++= Common.commonDependencies

scalaVersion := "2.11.8"

scalariformSettings

routesGenerator := InjectedRoutesGenerator
