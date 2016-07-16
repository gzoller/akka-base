package com.foo

import scala.sys.process._

object RunMe extends App {

	val iap = IpAndPort()
	val external = iap.akkaPort
	val host = iap.hostIP

	println("From inside the Docker container the on-board portster service says...")
	println(s"   Akka's port 2551 is mapped externally to port $external")
	println(s"   and we're running on physical host $host")
}