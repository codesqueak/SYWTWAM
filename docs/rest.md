# HTTP METHODS

| HTTP Verb     | CRUD             | Singleton / Collection | Idempotent | Safe  | Action                                                                         |
| ------------- |------------------|------------------------|------------|-------|--------------------------------------------------------------------------------|
| GET           | READ             | Singleton / Collection | Yes        | Yes   | Retrieve an entity identified by the Request-URI                               |
| HEAD          |                  | Singleton / Collection | Yes        | Yes   | As per GET but do not return body                                              |
| POST          | CREATE           | Singleton              | No         | No    | New / modified entity enclosed in the request as identified by the Request-URI |
| PUT           | UPDATE / REPLACE | Singleton              | Yes        | No    | Requests that the enclosed entity be stored under the supplied Request-URI     |
| DELETE        | DELETE           | Singleton / Collection | Yes (*1)   | No    | Requests that the server delete the resource identified by the Request-URI     | 
| TRACE         |                  |                        | No         | No    | Invoke a remote, application-layer loop-back of the request message            | 
| CONNECT       |                  |                        | No         | No    | Reserved                                                                       |
| OPTIONS       |                  | Singleton / Collection | Yes        | Yes   | Request for information about the communication options available              | 
| PATCH         | UPDATE / MODIFY  | Singleton              | No (*2)     | No   | Request to modify an existing entity                                           |      

(*1) Maybe ! - See notes on Delete further down

(*2) Not considered idempotent but can be made so by use of strict versioning. e.g use of ETag / If-Match

**Notes**

Safe Method : The convention has been established that the GET and HEAD methods SHOULD NOT have the significance of taking an action other than retrieval.

Idempotent :  In that (aside from error or expiration issues) the side-effects of N > 0 identical requests is the same as for a single request.

Patch :  [JSON Patch](https://tools.ietf.org/html/rfc6902) or [JSON Merge Patch](https://tools.ietf.org/html/rfc7396)

## GET 

Retrieve an entity identified by the Request-URI

| Response | Reason      | Notes                                                   |
|----------|-------------|---------------------------------------------------------| 
| 200      | OK          | Response entity in body                                 |
| 404      | NOT FOUND   | No matching entity exists                               | 
                                                                   |
## HEAD

Retrieves the header information of a resource. As per GET but do not return body

| Response | Reason      | Notes                                                   |
|----------|-------------|---------------------------------------------------------| 
| 204      | NO CONTENT  | Response entity body *MUST* be empty                    |
| 404      | NOT FOUND   | No matching entity exists                               | 
                                                                 
## POST

Most often utilized to create new resources. E.g. adding items to a collection.  **NOT IDEMPOTENT** 

| Response | Reason      | Notes                                                     |
|----------|-------------|-----------------------------------------------------------| 
| 201      | CREATED     | Entity body is the resource that was created              |
| 404      | NOT FOUND   | No matching entity exists, e.g. collection being added to |
| 409      | CONFLICT    | The resource already exists                               |
                                                                
## PUT

Can be used to:

1.  To create a resource in the case where the resource ID is chosen by the client 
2.  To update a known resource with the request body containing the newly-updated representation of the original resource

| Response | Reason      | Notes                                                   |
|----------|-------------|---------------------------------------------------------|
| 200      | OK          | Response entity in body                                 |
| 201      | CREATED     | Entity body is the resource that was created            |
| 204      | NO CONTENT  | Response entity body is empty                           |
| 404      | NOT FOUND   | No matching entity exists                               | 
| 412      | PRECON FAIL | For example, "if-Match" is invalid                               | 

## DELETE

Delete a resource

| Response | Reason      | Notes                                                   |
|----------|-------------|---------------------------------------------------------|
| 200      | OK          | Response entity in body                                 | 
| 204      | NO CONTENT  | Response entity body is empty                           |
| 404      | NOT FOUND   | No matching entity exists  (May not be permanent)       | 
| 410      | GONE        | No matching entity exists (Permanent)                   | 
| 412      | PRECON FAIL | For example, "if-Match" is invalid                               | 


Note: The exact semantics of delete can lead to various opinions.  For example, [this discussion](http://leedavis81.github.io/is-a-http-delete-requests-idempotent/)


## TRACE

Invoke a remote, application-layer loop-back of the request message

## CONNECT

Reserved

## OPTIONS

A request for information about the communication options available

## PATCH

 The PATCH request only needs to contain the changes to the resource, not the complete resource.
 The body contains a patch description [JSON Patch](https://tools.ietf.org/html/rfc6902) or [JSON Merge Patch](https://tools.ietf.org/html/rfc7396)

 Not considered idempotent but can be made so by use of strict versioning. e.g use of ETag / If-Match
 

| Response | Reason      | Notes                                                   |
|----------|-------------|---------------------------------------------------------|
| 200      | OK          | Response entity in body                                 |           |
| 204      | NO CONTENT  | Response entity body is empty                           |
| 404      | NOT FOUND   | No matching entity exists                               | 