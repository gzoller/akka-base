# akka-base
This container is designed to be used as a base for building Docker-ized Akka nodes.  Inside the base is a Go program called portster, which exposes an internally-facing REST service used to easily determine the external port mappings for Docker.  This knowledge is essential to make Akka's dual-binding work and allow Docker-ized Akka apps to communicate w/o hard-wiring the Dockers together.

The internal URL for the service is:

    GET localhost:1411/port/<port_num>;

##building
Run the build script in the project directory to build the Docker image.

##running
See the go.sh script for an example.  **Don't** just run go.sh though!  This image really not intended to run by itself but be a base to build upon.

##setup
There are a few things you'll need to do to make this work.  First, it is assumed you're going to build on top of this Docker--don't try to use it directly.  More to the point, you're going to override Endpoint, so you'll need to do some things.

You'll need to set the following environment variables somewhere in your start script inside your Docker (e.g. ash-template file if using sbt-native-packager):

* export DOCKER_HOST="tcp://$HOST_IP:2376"
* export DOCKER_CERT_PATH="/mnt/certs"

You'll also need to run the portster application:  `portster &`

###environment
When running your Docker build on akka-base, you'll need to mount the volume containing your certs to /mnt/certs and set the environment variable `DOCKER_TLS_VERIFY=true`.

For now you'll also need to set HOST_IP to the IP of the host running this Docker.  Currently this must be passed in but future versions will be be smart enough to detect that if you don't pass it in, the system will presume you're running on AWS and use AWS's discovery service to get the host's IP.  Coming soon!

A trivial example project using sbt-native-packager is provided in the example directory.  Note the inclusion of src/template/ash-template.  This is an override of the packager's default ash template except with the required changes to support portster.
