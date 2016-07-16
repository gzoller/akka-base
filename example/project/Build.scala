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
		dockerRepository      := Some("example")
		)

	// val akka_actor		= "com.typesafe.akka"		%% "akka-actor"		% Akka
	// val akka_slf4j 		= "com.typesafe.akka" 		%% "akka-slf4j"		% Akka
	// val akka_remote		= "com.typesafe.akka" 		%% "akka-remote"	% Akka
	// val typesafe_config	= "com.typesafe"			% "config"			% Config
	// val logback			= "ch.qos.logback" 			% "logback-classic"	% Logback
	// val slf4j_simple 	= "org.slf4j" 				% "slf4j-simple" 	% Slf4j

	lazy val root = Project(id = "example", base = file("."))
		.enablePlugins(JavaAppPackaging)
		.settings(dockerStuff:_*)
		.settings(
			dockerExposedPorts := Seq(2551),
			dockerEntrypoint   := Seq("/init","bin/example")
			)
		.settings(basicSettings: _*)
		// .settings(libraryDependencies ++= Seq(typesafe_config, akka_actor, akka_remote, akka_slf4j, logback))
}