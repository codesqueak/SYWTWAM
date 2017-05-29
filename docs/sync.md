# Sync Services

Building a basic RESTful web service with Spring Boot is a very simple process.  Spring documentation supplies an example that should get 
you up and running in about 15 minutes.

Spring Boot [Hello World](https://spring.io/guides/gs/rest-service/) example.

Building a production web service, while involving a higher degree of complexity, follows a very similar path.

# Project Sync Web Service Example

The project implements a service that allows the storage and retrieval of fortune cookie type quotes, with or without associated authors. The full set of operations
available are:


| Operation         | HTTP Verb | Implemented Yet ?  | Notes                                    |
|:------------------|:---------:|:------------------:|:-----------------------------------------|
| Read a fortune    | GET       | Yes                | Uses UUID to identify selected fortune   |
| Read header       | HEAD      | Yes                | As above but returns header info only    |
| Create a fortune  | POST      | Yes                | Server generates UUID                    |
| Upsert a fortune  | PUT       | Yes                | User specified the UUID                  |
| Partial update    | PATCH     | No                 | Two possible methods to do this          |
| Delete a fortune  | DELETE    | Yes                | Uses UUID to identify selected fortune   |
| Find options      | OPTIONS   | Yes                | Not usually used as part of REST         |
| Paged read        | GET       | Yes                | Specify page number and size             |


This basic set of operations is defined in the following interface and acts as a basis for all RESTful services
```Java
package com.codingrodent.microservice.template.api;

public interface IREST<K, V> {}
```

where K is the Key type and V is the resource Value type.

## Fortune Service Extensions

Besides the basic operations defined in ```interface IREST<K, V>```, further operations are defined in the following interface

```Java
public interface IFortune<K, V> extends IREST<K, V> {}

```
| Operation              | HTTP Verb | Implemented Yet ?  | Notes                                                 |
|:-----------------------|:---------:|:------------------:|:------------------------------------------------------|
| Filtered read - named  | GET       | Yes                | A paged read that only returns resources with authors |
| Filtered read - anon   | GET       | Yes                | A paged read that only returns anonymous authors      |


## Implementation

The ```interface IFortune<K, V>``` is implemented in a controller service
```Java
package com.codingrodent.microservice.template.controller;


@RestController
@Api(tags = "sync", value = "syncfortune", description = "Endpoint for fortune management")
@RequestMapping("/sync/fortune/" + API_VERSION)
public class SyncFortuneController extends RestBase<Fortune> implements IFortune<UUID, Fortune> {}
```
Where the Key type is UUID and the Value type is Fortune.  The Fortune class is annotated to be transformed to / from JSON.  Spring can manage this translation process automatically
which makes sending and receiving JSON request / response bodies a trivial process.

## A Brief Overview of Annotations Used

### Class Level Definition

*@RestController* - Tell Spring this is a specific controller for handling a RESTful web service

*@Api* - Describe the service to Swagger

*@RequestMapping* - Define the URI on which this service resides

### Method Level Definition

*@ApiParam* - Describe the parameter to Swagger

*@PathVariable* - Map a value contained in a path to a parameter in the method call, e.g. http://myapp.com/my/rest/service/{mapthisvalue}

*@RequestHeader* - Map a value contained in a header field to a parameter in the method call, e.g. Get a an eTag value

*@RequestBody* - Map a value contained in a request body to a parameter in the method call. Often used to transfer a JSON document to a matching class

*@RequestParam* -  Map a value contained in a request parameter to a parameter in the method call, e.g. when specifying a page value http://myapp.com/my/rest/service?page=123

