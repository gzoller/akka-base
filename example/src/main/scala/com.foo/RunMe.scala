package com.foo

import scala.sys.process._

object RunMe extends App {
	def get( uri:String ) = scala.io.Source.fromURL(uri,"utf-8").getLines.fold("")( (a,b) => a + b )

	val external = get("http://127.0.0.1:1411/port/2552")

	println("From inside the Docker container the on-board portster service says...")
	println(s"   Akka's port 2552 is mapped externally to port $external")
}