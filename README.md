# akka-base
This container is designed to be used as a base for building Docker-ized Akka nodes.  Inside the image is a Go program called portster, which exposes an internally-facing REST service used to determine the external port mappings for Docker.  This knowledge is essential to make Akka's dual-binding work and allow Docker-ized Akka apps to communicate w/o hard-wiring the Dockers together.  This is essential for clustering Docker-ized Akka nodes.

The internal URL for the service is:

    GET localhost:1411/port/<port_num>;

##Building
Run the *build* script in the project directory to build the Docker image.

## Running
```bash
docker run -it -P  -v /var/run/docker.sock:/var/run/docker.sock <your_image_here>
```
portster requires access to Docker internals to perform its function of port mapping introspection.  This is done via a UNIX socket provided by Docker's daemon process, but we must mount this socket to a known point inside the Docker, which is what the -v parameter is doing here.  Otherwise the parameters are as above.

##Example Scala App
A trivial example project based on gzoller/akka-base using sbt-native-packager is provided in the example directory.  You can publish it locally with (from the example directory):
```bash
sbt docker:publishLocal
```

##A word about init
Normally in a Dockerfile you specify some ENTRYPOINT, typically a program or script to run when the image starts.  akka-base is intended to be a base layer and is designed to have more layers on top of it.

Docker doesn't have a true POSIX init capability out-of-the-box, but akka-base includes S6-overlay, an init-like capability.  Read about it here: https://github.com/just-containers/s6-overlay

portster is started by S6 *if*...  You start the init script on Docker image start.  See the example app (project/Build.scala) and note that dockerEntrypoint is set to Seq("/init",""bin/example").  You'll always need to start /init first, then whatever your program is, or portster won't launch when your Docker starts.  Of course your images can create other S6 modules too if desired and they'll be automatically started by /init, thus creating the "layering" affect where upper layers don't know about the particulars of lower layers.
