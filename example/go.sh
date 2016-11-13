#!/bin/bash

# On a Mac
HOST_IP=`ifconfig en0 | grep inet | grep -v inet6 | awk '{print $2}'`

# Presuming native Docker (eg. Mac) and standard docker.sock location
# If HOST_IP not passed in akka-base will presume AWS deployment and use AWS to get host's IP
docker run -it -P -v /var/run/docker.sock:/var/run/docker.sock -e HOST_IP=$HOST_IP gzoller/example:0.1-SNAPSHOT