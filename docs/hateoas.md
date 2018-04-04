# HATEOAS - Hypermedia As The Engine Of Application State

The HATEOAS constraint decouples client and server in a way that allows the server functionality to evolve independently._

## Spring Support for HATEOAS

With Spring, implementing HATEOAS support to give a hypermedia-driven REST service is relatively easy process via the use of 
the [Spring HATEOAS API](http://projects.spring.io/spring-hateoas/). 

Use of this within a Spring Boot, a detailed [Getting Started](https://spring.io/guides/gs/rest-hateoas/) project is 
available which shows how to use the API to easily create links pointing to Spring MVC controllers

## Example Implementation - Sync

With a synchronous service, the technique is to add a [Link](http://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/Link.html) to the 
Resource being returned by the controller. For example:

```Java
    String uuid = "a34f55a6-b1e8-4212-a91a-7981d35202e6";
    Fortune fortune = new Fortune("New text","New author");
    fortuneService.save(uuid, fortune);
    Resource<Fortune> resource = new Resource<>( fortune );
    resource.add(linkTo(methodOn(FortuneController.class).read(uuid)).withSelfRel(););
```

Which would return the following JSON

```json
{
  "text": "New text",
  "author": "New author",
  "_links": {
    "self": {
      "href": "http://localhost:8081/sync/fortune/V1/a34f55a6-b1e8-4212-a91a-7981d35202e6"
    }
  }
}
```

## Example Implementation - Async

The basic principle used with sync controllers can be used with async controller, i.e. add a link to the returned resource.  However, a complexity exists in that the thread
adding the link may not be the same thread as that on which the original call is made.  This causes a problem as the information required by the HATEOAS API to 
generate links is associated with the thread. Different thread, no data - fail!

The way to circumvent this is to store the required information somewhere it can be recovered later in the Observable call chain.  To do this we have a custom operator than in effect does
nothing except act as a pass through which saves and restores state.

```java
public class SaveStateOperator<T> implements Observable.Operator<T, T> {

    private final RequestAttributes requestAttributes;

    /**
     * Grab the state of the request attributes at construction time.
     * This makes them available for restoration at any point in the future on whatever thread 
     * is calling the onNext() method
     */
    public SaveStateOperator() {
        requestAttributes = RequestContextHolder.currentRequestAttributes();
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(T t) {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                subscriber.onNext(t);
            }
        };
    }
}
```
When the operator is created, it grabs the current request state which is available as this is executing on the calling thread.
When it is later called upon to process a response, _onNext()_, it restores the state. Which thread is being used at this point is now immaterial. 

Our custom operator _saveStateOperator_ can then be used in an Observable chain via a lift() operation to save / restore state.
```java
DeferredResult<Resource<Fortune>> result = new DeferredResult<>();
        fortuneService.load(uuid.toString()).
                lift(new SaveStateOperator<>()).
                map(ModelVersion::getModel).
                map(fortune -> new Resource<>(fortune, getRelLink(fortune))).
                subscribe(result::setResult, (t) -> result.setErrorResult(new RuntimeException(t)));
        return result;

```








