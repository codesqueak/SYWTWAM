#Configuration Using Consol

Download [Consul](https://www.consul.io/) from Hashicorp 

consul agent  -server -ui -data-dir=data -config-dir=config  -bind 127.0.0.1 -bootstrap -log-level=debug

localhost:8500


## bootstrap.properties
## consul setup
spring.cloud.consol.host=localhost
spring.cloud.consol.port=8500
## fail immediately if problem
spring.cloud.consul.config.failFast=true


## Use the config doscovery service
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server

## profiles
spring.profiles.active=prod,zzz


This would give a search onto the following keys for  @Value("${xyzzy}") in the following order

Request GET /v1/kv/config/sywtwam,zzz/
Request GET /v1/kv/config/sywtwam,prod/
Request GET /v1/kv/config/sywtwam/
Request GET /v1/kv/config/application,zzz/
Request GET /v1/kv/config/application,prod/
Request GET /v1/kv/config/application/



