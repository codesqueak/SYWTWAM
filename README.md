#So You Want To Write A Microservice

As you all know, writing a  microservice is a trivial task. Yeah! I'll rephrase that. Writing a minimal example microservice that never gets deployed in a 
live environment is a trival task.

This project demonstrates a set of possible technologies(*) working together to build and deploy a **real** microservice. As the project proceeds, this list 
will change, technologies will be substituted and I will change things in the light of experience.

(*) Only one example of each. I'm not doing every tool, framework and language in existence. Life is too short to ~~stuff a mushroom~~ follow new Node.js 
frameworks.

To give you an idea of the problem, this is my basic list of things to get to grips with to build a **real** microservice. This is aimed at building Java 
applications for an AWS environment.  

1. Is this a definitive list? - No.
2. Are these recommended tools & technologies? - No, just possible examples. Use whatever floats your boat.
3. Do I need everything here? - No. But it makes a useful initial checklist to see if you have left anything out.



##The Microservice
| Task            | Possible Implementation Technology  |
|-----------------|---------------------|
| Basic framework | [SpringBoot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)|
| Configuration | [Spring](http://spring.io/) |
| Async services | [RxJava](https://github.com/ReactiveX/RxJava) |
| Sync services | [Java](https://www.java.com)  |
| API Framework /  Documentation | [Swagger](http://swagger.io/)|
| Error handling |  |
| Validation | [JSR303 (Hibernate Validator)](http://hibernate.org/validator/) |
| Circuit breaker | [Hystrix](https://github.com/Netflix/Hystrix) |
| Fallback  | [Hystrix](https://github.com/Netflix/Hystrix) |
| Fail Fast | [Hystrix](https://github.com/Netflix/Hystrix) |
| Bulkhead | [Hystrix](https://github.com/Netflix/Hystrix) |
| Logging | [logback](http://logback.qos.ch/) / [log4j](http://logging.apache.org/log4j/2.x/) |
| Crypto /JCA | [Spring Security](http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html) |
| SSL / x509 handling | [Spring Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/) |
| HATEOAS | [Spring HATEOAS](http://docs.spring.io/autorepo/docs/spring-hateoas/0.20.x/reference/html/) |
| Authorization / JWT | [Spring Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/) |
| JSON | [Jackson](https://github.com/FasterXML/jackson) / [gson](https://github.com/google/gson) |
| Persistence | [Spring Data](http://projects.spring.io/spring-data/) |
| Web caching | Cache control headers (ETag etc) |



##Test Test Test ... !
| Task            | Possible Implementation Technology  |
|-----------------|---------------------|
| Build | [Maven](https://maven.apache.org/) / [Gradle](https://gradle.org/)  |
| Unit test | [JUnit](http://junit.org/junit4/) |
| Integration test | [JUnit](http://junit.org/junit4/) / [Scala Test](http://www.scalatest.org/) |
| Performance analysis | [YourKit](https://yourkit.com) |
| Load test | [Bees with machine guns](https://github.com/newsapps/beeswithmachineguns) / [The Grinder](http://grinder.sourceforge.net/) |
| Stress test | [Chaos monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey) |
| Security audit | [Coverity](http://www.coverity.com/products/code-advisor/) |
| Code quality | [PMD](https://pmd.github.io/) |
| Static analysis | [findbugs](http://findbugs.sourceforge.net/) |
| CI | [Jenkins](https://jenkins.io/) / [Bamboo](https://www.atlassian.com/software/bamboo) |
| Mocking | [Mockito](http://site.mockito.org/) / [EasyMock](http://site.mockito.org/) |
| Mock Services | [WireMock](http://wiremock.org/) |


## Infrastructure 
| Task            | Possible Implementation Technology  |
|-----------------|---------------------|
| Cloud | [AWS](https://aws.amazon.com/) ... There are others ... ? |
| Firewall | Loads to choose from  | 
| Routing | [Zuul](https://github.com/Netflix/zuul) |
| Containers | [Docker](https://www.docker.com/) |
| Configuration | [Terraform](https://www.terraform.io/) |
| Load balancer | |


## Environment 
| Task            | Possible Implementation Technology  |
|-----------------|---------------------|
| Data storage / persistence | [MongoDB](https://www.mongodb.com/) / [Coucbase](http://www.couchbase.com/) / [PostgrSQL](https://www.postgresql.org/)  |
| Cache | [Hazelcast](https://hazelcast.com/) / [memcached](https://memcached.org/)  |
| Logging | [ELK (Elastic search / Logstash / Kibana)](https://www.elastic.co/webinars/introduction-elk-stack)  |
| Message bus | [RabbitMQ](https://www.rabbitmq.com/)   |
| Event bus  |  |
| Configuration | [Consul](https://www.consul.io/)  |
| Authentication | [OAuth2](https://oauth.net/2/)  |
| Monitoring and analytics | [New Relic](https://newrelic.com/) / [AppDynamics](https://www.appdynamics.com/)  |
| Service discovery | [Eureka](https://github.com/Netflix/eureka)  |


## Deployment
| Task            | Possible Implementation Technology  |
|-----------------|---------------------|
| CSCI store | [Artifactory](https://www.jfrog.com/artifactory/) / [Sonatype Nexus](https://www.sonatype.com/nexus-repository-sonatype) |
| Build | [Puppet](https://puppet.com/) / [Ansible](https://www.ansible.com)  |
| Containers | [Docker](https://www.docker.com/)  |
| Orchestration | [Kubernetes](http://kubernetes.io/) / [Nomad](https://www.hashicorp.com/blog/nomad.html) / [Docker Swarm](https://docs.docker.com/swarm/)  |






