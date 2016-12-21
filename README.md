[![License: MIT](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)

#So You Want To Write A Microservice

As you all know, writing a  microservice is a trivial task. Yeah! I'll rephrase that. Writing a trivial example microservice that never gets deployed in a 
live environment is a trival task.

This project sets out to demonstrate a group of possible technologies (1) working together to build and deploy a **real** microservice. As the project 
proceeds, this list will change, technologies will be substituted and I will modofy things in the light of experience.

(1) Only one example of each. I'm not doing every tool, framework and language in existence. Life is too short to ~~follow new Node.js frameworks~~ try every 
possible combination.

To give you an idea of the problem, this is my basic list of things to get to grips with to build a **real** microservice. This is aimed at building Java 
applications for an AWS environment.  

1. Is this a definitive list? - No.
2. Are these recommended tools & technologies? - No, just possible examples. Use whatever floats your boat.
3. Do I need everything here? - No. But it makes a useful initial checklist to see if you have left anything out.



##The Microservice
| Task            | Possible Implementation Technology  | Status |
|-----------------|---------------------|---------------------|
| Basic framework | [SpringBoot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)| ![alt text](docs/images/tick.png "Implemented") |
| Configuration | [Spring](http://spring.io/) | ![alt text](docs/images/tick.png "Implemented") |
| Async services | [RxJava](https://github.com/ReactiveX/RxJava) | ![alt text](docs/images/cross.png "Not Implemented") |
| Sync services | [Java](https://www.java.com)  | ![alt text](docs/images/cross.png "Not Implemented") |
| API Framework /  Documentation | [Swagger](http://swagger.io/) / [SpringFox](http://springfox.github.io/springfox/)| ![alt text](docs/images/cross.png "Not Implemented") |
| Error handling |  | ![alt text](docs/images/cross.png "Not Implemented") |
| Validation | [JSR303 (Hibernate Validator)](http://hibernate.org/validator/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Circuit breaker | [Hystrix](https://github.com/Netflix/Hystrix) | ![alt text](docs/images/cross.png "Not Implemented") |
| Fallback  | [Hystrix](https://github.com/Netflix/Hystrix) | ![alt text](docs/images/cross.png "Not Implemented") |
| Fail Fast | [Hystrix](https://github.com/Netflix/Hystrix) | ![alt text](docs/images/cross.png "Not Implemented") |
| Bulkhead | [Hystrix](https://github.com/Netflix/Hystrix) | ![alt text](docs/images/cross.png "Not Implemented") |
| Logging | [logback](http://logback.qos.ch/) / [log4j](http://logging.apache.org/log4j/2.x/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Crypto /JCA | [Spring Security](http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html) | ![alt text](docs/images/cross.png "Not Implemented") |
| SSL / x509 handling | [Spring Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/) | ![alt text](docs/images/cross.png "Not Implemented") |
| HATEOAS | [Spring HATEOAS](http://docs.spring.io/autorepo/docs/spring-hateoas/0.20.x/reference/html/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Authorization / JWT | [Spring Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/) | ![alt text](docs/images/cross.png "Not Implemented") |
| JSON | [Jackson](https://github.com/FasterXML/jackson) / [gson](https://github.com/google/gson) | ![alt text](docs/images/cross.png "Not Implemented") |
| Persistence | [Spring Data](http://projects.spring.io/spring-data/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Web caching | Cache control headers (ETag etc) | ![alt text](docs/images/cross.png "Not Implemented") |



##Test Test Test ... !
| Task            | Possible Implementation Technology  | Status |
|-----------------|---------------------|---------------------|
| Build | [Maven](https://maven.apache.org/) / [Gradle](https://gradle.org/)  | ![alt text](docs/images/tick.png "Implemented") |
| Unit test | [JUnit](http://junit.org/junit4/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Integration test | [JUnit](http://junit.org/junit4/) / [Scala Test](http://www.scalatest.org/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Performance analysis | [YourKit](https://yourkit.com) | ![alt text](docs/images/cross.png "Not Implemented") |
| Load test | [Bees with machine guns](https://github.com/newsapps/beeswithmachineguns) / [The Grinder](http://grinder.sourceforge.net/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Stress test | [Chaos monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey) | ![alt text](docs/images/cross.png "Not Implemented") |
| Security audit | [Coverity](http://www.coverity.com/products/code-advisor/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Code quality | [PMD](https://pmd.github.io/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Static analysis | [findbugs](http://findbugs.sourceforge.net/) | ![alt text](docs/images/cross.png "Not Implemented") |
| CI | [Jenkins](https://jenkins.io/) / [Bamboo](https://www.atlassian.com/software/bamboo) | ![alt text](docs/images/cross.png "Not Implemented") |
| Mocking | [Mockito](http://site.mockito.org/) / [EasyMock](http://site.mockito.org/) ... and all the others | ![alt text](docs/images/cross.png "Not Implemented") |
| Mock Services | [WireMock](http://wiremock.org/) | ![alt text](docs/images/cross.png "Not Implemented") |


## Infrastructure 
| Task            | Possible Implementation Technology  | Status |
|-----------------|---------------------|---------------------|
| Cloud | [AWS](https://aws.amazon.com/) ... There are others ... :) | ![alt text](docs/images/cross.png "Not Implemented") |
| Firewall | Loads to choose from  |  ![alt text](docs/images/cross.png "Not Implemented") |
| Routing | [Zuul](https://github.com/Netflix/zuul) | ![alt text](docs/images/cross.png "Not Implemented") |
| Containers | [Docker](https://www.docker.com/) | ![alt text](docs/images/tick.png "Implemented") |
| Configuration | [Terraform](https://www.terraform.io/) | ![alt text](docs/images/cross.png "Not Implemented") |
| Load balancer | | ![alt text](docs/images/cross.png "Not Implemented") |


## Environment 
| Task            | Possible Implementation Technology  | Status |
|-----------------|---------------------|---------------------|
| Data storage / persistence | [MongoDB](https://www.mongodb.com/) / [Coucbase](http://www.couchbase.com/) / [PostgrSQL](https://www.postgresql.org/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Cache | [Hazelcast](https://hazelcast.com/) / [memcached](https://memcached.org/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Logging | [ELK (Elastic search / Logstash / Kibana)](https://www.elastic.co/webinars/introduction-elk-stack)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Message bus | [RabbitMQ](https://www.rabbitmq.com/)   | ![alt text](docs/images/cross.png "Not Implemented") |
| Event bus  |  | ![alt text](docs/images/cross.png "Not Implemented") |
| Configuration | [Consul](https://www.consul.io/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Authentication | [OAuth2](https://oauth.net/2/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Monitoring and analytics | [New Relic](https://newrelic.com/) / [AppDynamics](https://www.appdynamics.com/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Service discovery | [Eureka](https://github.com/Netflix/eureka)  | ![alt text](docs/images/cross.png "Not Implemented") |


## Deployment
| Task            | Possible Implementation Technology  | Status |
|-----------------|---------------------|---------------------|
| CSCI store | [Artifactory](https://www.jfrog.com/artifactory/) / [Sonatype Nexus](https://www.sonatype.com/nexus-repository-sonatype) | ![alt text](docs/images/cross.png "Not Implemented") |
| Build | [Puppet](https://puppet.com/) / [Ansible](https://www.ansible.com)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Containers | [Docker](https://www.docker.com/)  | ![alt text](docs/images/cross.png "Not Implemented") |
| Orchestration | [Kubernetes](http://kubernetes.io/) / [Nomad](https://www.hashicorp.com/blog/nomad.html) / [Docker Swarm](https://docs.docker.com/swarm/)  | ![alt text](docs/images/cross.png "Not Implemented") |






