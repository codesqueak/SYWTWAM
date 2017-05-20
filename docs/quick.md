# Quick Start

What you need to do to build this project

## Install

The application requires that the [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [Couchbase](https://www.couchbase.com/) are 
installed. Couchbase is used as the storage engine in the examples.

Once Couchbase has been installed, create a Data Bucket named **template** with the password **password**

These settings may be changed by modifying the following values in application.properties
```
spring.couchbase.bucket.name=template
spring.couchbase.bucket.password=password
```

If Windows is being used, [git](https://www.atlassian.com/git/tutorials/install-git/windows) will need to be installed.


## Clone

Get the application
```
git clone https://github.com/codesqueak/SYWTWAM.git
```


## Build

For windows: ```gradlew clean build test```

For Linux: ```./gradlew clean build test```

This will pull down all required libraries, compile and test the application.


## Run


For Windows: ```java -Dspring.cloud.consul.enabled=false -Dspring.cloud.bus.enabled=false -jar app/build/libs/SYWTWAM-0.2.1-SNAPSHOT-BOOT.jar --spring.profiles.active=aws```

For Linux: ```java -Dspring.cloud.consul.enabled=false -Dspring.cloud.bus.enabled=false -jar app/build\libs\SYWTWAM-0.2.1-SNAPSHOT-BOOT.jar --spring.profiles.active=aws```

### Notes

If you have configured Couchbase, you can run without the **--spring.profiles.active=aws** option

If you have configured Consul, you can run without the **-Dspring.cloud.consul.enabled=false -Dspring.cloud.bus.enabled=false** options


## Play

The Swagger interface to the application is now available at: ```http://localhost:8081/swagger-ui.html```