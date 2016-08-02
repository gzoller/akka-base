#!/bin/bash

ACTIVE=`docker-machine active 2>/dev/null`
HOST_IP=`docker-machine ip $ACTIVE`

docker run -it -P -v ~/.docker/machine/certs:/mnt/certs -e "DOCKER_TLS_VERIFY=true" -e HOST_IP=$HOST_IP gzoller/example:0.1-SNAPSHOT
