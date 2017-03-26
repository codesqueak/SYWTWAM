# Spring Boot

The project is built using the [Spring Boot](https://projects.spring.io/spring-boot/) framework. Use is made of several [_spring-boot-starter_](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters)
 projects to supply significant areas of functionality with minimal work.

Specifically the following components are used:

1. **spring-boot-starter-web** Starter for building web, including RESTful, applications using Spring MVC
2. **spring-boot-starter-undertow** - Starter for using Undertow as the embedded servlet container
3. **spring-boot-starter-actuator** - Starter for using Spring Boot's Actuator which provides production ready features to help you monitor and manage your application 
4. **spring-boot-starter-test** - Starter for testing Spring Boot applications

## spring-boot-starter-web

This ‘[Starter](https://spring.io/guides/gs/spring-boot/)’ supplies basic functionality for using Spring Web Services 


## spring-boot-starter-undertow

This ‘[Starter](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-embedded-servlet-containers.html)’ allows the use of the [Undertow](http://undertow.io/) high performance web server rather than
 the default Tomcat container


## spring-boot-starter-actuator

This ‘[Starter](https://spring.io/guides/gs/actuator-service/)’ supplies various Spring Boot additional features to help you monitor and manage your application when it’s in production. 
Monitoring and management can be done via HTTP endpoints, MX, SSH and Telnet. Monitoring includes auditing, health information and various metrics which may 
be gathered.

## spring-boot-starter-test

This ‘Starter’ supplies utilities, libraries and annotations to help in testing a Spring Boot application.  This includes:

1. [JUnit](http://junit.org/junit4/) — The de-facto standard for unit testing Java applications.
2. [Spring Test & Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)— Utilities and integration test support for Spring Boot applications.
3. [AssertJ ](http://joel-costigliola.github.io/assertj/)— A fluent assertion library.
4. [Hamcrest](http://hamcrest.org/) — A library of matcher objects (also known as constraints or predicates).
5. [Mockito](http://site.mockito.org/) — A Java mocking framework.
6. [JSONassert](http://jsonassert.skyscreamer.org/) — An assertion library for JSON.
7. [JsonPath](https://github.com/jayway/JsonPath) — XPath for JSON.

## Gradle

The build system for the project is [Gradle](https://gradle.org/).  To include the starters, the following dependencies are specified:

```groovy
dependencies {
    // Spring boot
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-web', version: '1.4.3.RELEASE'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-undertow', version: '1.4.3.RELEASE'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-actuator', version: '1.4.3.RELEASE'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-test', version: '1.4.3.RELEASE'
    // ...
    }
```





