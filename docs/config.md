# Configuration

One problem with applications executing in a cloud type environment is separating the application away from its configuration.  With a typical Spring application, configuration is 
obtained from the application.properties.  What happens when configuration information is not available at build time ? The solution is to hold this information in the 
environment in some form.


## Consol from HashiCorp

One possible solution is to use a service such as Consol which supplies a distributed key / value store which simply integrates into Spring Boot applications.

Consul supplies:

- Service Discovery
- Failure Detection
- Scalability 
- Key / Value Storage (*The bit we are interested in ...* :smile: )

## Install

Download [Consul](https://www.consul.io/) from Hashicorp and unpack where you want to run it from.  This is a simple executable file.

Consul can be run in two modes. As a client (which must be present on any node wanting to use the Consul service), or as a server, which talks to other servers in the
cluster setup and clients.  To make life simple, the server can be brought up on its own to support applications on the same node.  For full 
documentation on how to configure a cluster look [here](https://www.consul.io/intro/getting-started/install.html)

To start, make sure you have a */config* directory where you unpacked consul (empty is fine), and then use this command:
```
consul agent  -server -ui -data-dir=data -config-dir=config  -bind 127.0.0.1 -bootstrap -log-level=debug
```
- agent - start the agent
- -server - start the server
- -ui - enable support of the management UI
- -data-dir - location where Consul can store its data
- -config-dir - location where Consul reads configuration information from (if required)
- -bind - address for internal cluster communications
- -boostrap - enable boostrap mode. Allows a single node to be brought up
- -log-level - recommend 'debug' at first as allows for easy application debug as the message flowing back and forth are visible

Once startup has completed, opne http://localhost:8500 to access the management UI

## Spring Boot

Spring Boot is capable of supporting cloud configuration using Consul 'out of the box' by adding the following to the application build path. Docs [here](https://cloud.spring.io/spring-cloud-consul/)

```gradle
compile group: 'org.springframework.cloud', name: 'spring-cloud-starter', version: '1.1.7.RELEASE'
compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-consul-all', version: '1.1.2.RELEASE'
```

To start, the application requires information on where to look for configuration information.  This is set in a boostrap.configuration file which Spring picks up on startup.  
This is located in the same location as application.properties.

Various configuration parameters can be set:

| Setting                                | Value          | Action                                              |
|-----------------------------------------|---------------|-----------------------------------------------------|
| spring.cloud.consol.host                | localhost     | Client address                                      | 
| spring.cloud.consol.port                | 8500          | Client port                                         |     
| spring.cloud.consul.config.failFast     | true          | Fail immediately if problem ?                       |   
| spring.cloud.config.discovery.enabled   | true          | Use discovery service to location configuration     |     
| spring.cloud.config.discovery.serviceId | config-server | Config server name                                  |     

If you prefer, this can be specified as a YAML script in the boostrap.yaml file


## Key / Value Store

Any configuration value that is to be stored for the application, must be held on a specific path.  For example, using the following Spring argument injector:

```
@Value("${xyzzy}")
```

The value of 'xyzzy' would be held under the following path:

```
/v1/kv/config/sywtwam/
```
If a Spring Profile is active, then this is added as an addition to the application name.  For example, if the profile *prod* was being used, the paths searched would be:


```
/v1/kv/config/sywtwam,prod/
/v1/kv/config/sywtwam/
```
If multiple profiles are used, for example *prod,zzz* then the paths searched would be:

```
/v1/kv/config/sywtwam,zzz/
/v1/kv/config/sywtwam,prod/
/v1/kv/config/sywtwam/
```
It is also possible to store values which are globally available. After searching the *sywtwam* paths, the following paths are consulted:

```
/v1/kv/config/application,zzz/
/v1/kv/config/application,prod/
/v1/kv/config/application/
```
It is possible to set custom values for parts of the paths. Paths are defined as:

```
/v1/kv/{spring.cloud.consul.config.prefix}/{spring.application.name}/
/v1/kv/{spring.cloud.consul.config.defaultContext}/{spring.application.name}/

```
If profiles are being used, the name seperator can be defined using spring.cloud.consul.config.profileSeparator

## Example Configuration - bootstrap.properties

```
spring.application.name=sywtwam
## profiles
spring.profiles.active=prod,zzz
## consul setup
spring.cloud.consol.host=localhost
spring.cloud.consol.port=8500
## enable config via consul
spring.cloud.consul.config.enabled=true
## Use the config doscovery service
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
## fail immediately if problem
spring.cloud.consul.config.failFast=true
## name prefix GET /v1/kv/{spring.cloud.consul.config.prefix}/{spring.application.name}
spring.cloud.consul.config.prefix=config
spring.cloud.consul.config.defaultContext=apps
spring.cloud.consul.config.profileSeparator=::
```
Gives the following set of search paths:
```
/v1/kv/config/sywtwam::zzz/
/v1/kv/config/sywtwam::prod/
/v1/kv/config/sywtwam/
/v1/kv/config/apps::zzz/
/v1/kv/config/apps::prod/
/v1/kv/config/apps/
```

