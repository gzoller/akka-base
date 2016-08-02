# akka-base
This container is designed to be used as a base for building Docker-ized Akka nodes.  Inside the base is a Go program called portster, which exposes an internally-facing REST service used to easily determine the external port mappings for Docker.  This knowledge is essential to make Akka's dual-binding work and allow Docker-ized Akka apps to communicate w/o hard-wiring the Dockers together.  This is essential for clustering Docker-ized Akka nodes.

The internal URL for the service is:

    GET localhost:1411/port/<port_num>;

##Building
Run the build script in the project directory to build the Docker image.

## Two Ways to Run
If you're running on a docker-machine the out-of-the-box Docker configuration uses TLS security.  Portster (and hence akka-base) supports TLS but you'll need to provide some passed-in parameters when running your image.  If you're running in a non-TLS environment, for example in AWS, you'll need different parameters, so let's look at each separately

###1) TLS (docker-machine) 
See the go.sh script for an example.  **Don't** just run go.sh though!  This image really not intended to run by itself but be a base to build upon.  Note the main line in the script:

```bash
docker run -it -P -v ~/.docker/machine/certs:/mnt/certs -e "DOCKER_TLS_VERIFY=true" -e HOST_IP=$HOST_IP <your_image_here>
```
Let's pick it apart.

|Part  |Explanation   |
|---|---|
|docker run -it  |Self-explanatory|
|-P   |Auto-assign any EXPOSE ports|
|-v ~/.docker/machine/certs:/mnt/certs|Mount security certs to a point inside the Docker|
|-e "DOCKER_TLS_VERIFY=true"|Tell image to use TLS security|
|-e HOST_IP=$HOST_IP|Pass in the IP of the physical host|
|<< your_image_here >>|Some image based on akka-base

###2) Non-TLS (AWS)
```bash
docker run -it -P  -v /var/run/docker.sock:/var/run/docker.sock <your_image_here>
```
For non-TLS environments there's no security to worry about but we still need to allow portster access to Docker internals.  This is done via a UNIX socket, but we must mount this socket to a known point inside the Docker, which is what the -v parameter is doing here.  Otherwise the parameters are as above.

##Example Scala App
A trivial example project based on gzoller/akka-base using sbt-native-packager is provided in the example directory.  You can publish it locally with (from the example directory):
```bash
sbt docker:publishLocal
```

##A word about init
Normally in a Dockerfile you specify some ENTRYPOINT, typically a program or script to run when the image starts.  akka-base is intended to be a base layer and is designed to have more layers on top of it.

By default, Docker doesn't have a true POSIX init capability but akka-base includes S6-overlay, an init-like capability.  Read about it here: https://github.com/just-containers/s6-overlay

portster is started by S6 *if*...  You start the init script on Docker image start.  See the example app (project/Build.scala) and note that dockerEntrypoint is set to Seq("/init",""bin/example").  You'll always need to start /init first, then whatever your program is, or portster won't launch when your Docker starts.  Of course your images can create S6 modules too if desired and they'll be automatically started by /init.
