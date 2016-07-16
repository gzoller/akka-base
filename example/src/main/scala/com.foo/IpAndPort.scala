package com.foo

import java.net._
import java.io._

/**
 * Utility to determine the host's IP address and Akka port.  This takes into
 * account whether the caller is running in a Docker container or not, and if it is,
 * and it's running on AWS, it can use AWS' lookup service to determine the host IP.
 *
 * Likewise if the caller is in a Docker the port assignments must be interrogated,
 * which is handled by a REST call to the on-board portster module included in the
 * akka-base image.  This module looks up the Docker-generated port mapping for the
 * the "normal" Akka port (2551).
 */
case class IpAndPort(defaultAkkaPort: Int = 2551) {

  // Check for /.dockerenv file to see if we're inside a docker or not!
  private val inDocker = (new java.io.File("/.dockerenv")).exists()
  private val myIP = java.net.InetAddress.getLocalHost().getHostAddress()

  def get( uri:String ) = scala.io.Source.fromURL(uri,"utf-8").getLines.fold("")( (a,b) => a + b )

  lazy val akkaPort =
    if (inDocker) {
      println("NOTE: We're running inside a Docker!")
      get(s"http://$myIP:1411/port/2551").toInt
    }
    else {
      println("No Docker detected...")
      defaultAkkaPort
    }

  lazy val hostIP =
    if (inDocker) {
      println("NOTE: We're running inside a Docker!")
      get(s"http://$myIP:1411/hostip")
    }
    else {
      println("No Docker detected...")
      myIP
    }
}

class FailException(msg: String) extends Exception(msg)