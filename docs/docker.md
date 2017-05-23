# Docker

As part of the DevOps / cloud deployment goal, it is useful as an inintial step if we can configure the application to deploy to Docker as this gives
us a wide range of execution platforms and options.

# Install

Docker can be installed on Linux, Windows and Mac platforms.  Linux is by far the easiest.

* Windows (Before 10) - [Here](https://docs.docker.com/toolbox/toolbox_install_windows/) 
* Windows (Version 10) - [Here](https://docs.docker.com/docker-for-windows/install/) 
* Linux - ```sudo yum install docker``` or ```sudo apt-get docker``` depending on distro (You may need to google for it)
* Mac - [Here](https://docs.docker.com/docker-for-mac/)

## Generate a Docker Image

Creating a Docker image using Gradle is a simple task when using the [Transmode gradle-docker plugin](https://github.com/Transmode/gradle-docker). 
Spring has a detailed usage guide [here](https://spring.io/guides/gs/spring-boot-docker)

## Dockerfile

The file used to define the Docker image build is defined as

```
FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
EXPOSE 8081
ADD SYWTWAM-0.2.1-SNAPSHOT-BOOT.jar ./app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Dspring.cloud.consul.enabled=false","-Dspring.cloud.bus.enabled=false","-Dspring.profiles.active=aws","-jar","./app.jar"]
```

In summary, this deploys the application jar into a prebuilt image which includes Java 8, configures it to be able to use port 8081 and generates a command line 
to start everything.


The full Dockerfile reference can be found [here](https://docs.docker.com/engine/reference/builde)

# Build With Windows

Once Docker has been installed and is running, open a console window at the root directory of the project and issue the following command which
will build and deploy an image

```
gradlew clean build test buildDocker
```
 Running the command ``` docker images``` should show an image named *sywtwam*

# Build With Linux

Once Docker has been installed and is running, open a console window at the root directory of the project. At this point a small problem appears as Docker command that communicate with
the Docker daemon need to be run using *sudo*.  While this is acceptable for manual commands, it will cause problems with the Gradle build.  Docker can be made visible
to the present user by adding them to the *docker* group.
 ```
 sudo groupadd docker
 sudo gpasswd -a ${USER} docker
 sudo service docker restart
 ```
 
Try the command ```docker images``` to verify that everything is operational. If this doesn't work, try restarting the machine and manually starting the service.
 
Once the command runs without issue, open a console window at the root directory of the project and issue the following command which
will build and deploy an image
                                      
```
./gradlew clean build test buildDocker
```

Running the command ``` docker images``` should show an image named *sywtwam*
 
# Run

Now we have an image, running it is as simple as the following command:

```
docker run -d -p 8081:8081 sywtwam
```

This will start execution, in detached mode (-d) with internal port 8081 mapped to port 8081 externally (-p).  Check that the everything is running with:
```
docker ps
```
where you should see something like this:
```
ONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
f094fea7b3f2        sywtwam             "java -Dspring.cloud."   9 seconds ago       Up 7 seconds        0.0.0.0:8081->8081/tcp   awesome_noether
```

If all looks good, connect to Swagger on [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

To stop the executing image, use ```docker stop <ONTAINER ID>```



