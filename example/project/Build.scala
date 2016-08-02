import sbt._
import Keys._

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes._
import com.typesafe.sbt.packager.docker._

object Build extends Build {

	lazy val basicSettings = Seq(
		scalaVersion 				:= "2.11.7",
		scalacOptions				:= Seq("-feature", "-deprecation", "-encoding", "UTF8", "-unchecked"),
		testOptions in Test += Tests.Argument("-oDF")
	)

	lazy val dockerStuff = Seq(
		maintainer            := "John Smith <fake@nowhere.com>",
		dockerBaseImage       := "gzoller/akka-base",
		daemonUser in Docker  := "root",
		dockerRepository      := Some("gzoller")
		)

	lazy val root = Project(id = "example", base = file("."))
		.enablePlugins(JavaAppPackaging)
		.settings(dockerStuff:_*)
		.settings(
			dockerExposedPorts := Seq(2551),
			dockerEntrypoint   := Seq("/init","bin/example")
			)
		.settings(basicSettings: _*)
}