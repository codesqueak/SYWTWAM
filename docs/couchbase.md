# Couchbase

The example code uses spring-data-couchbase as its respository handler.  To support this, Couchbase needs to be installed and configured. 
This sets up a minimum development environment of a single node.

## Installation

Download and install Couchbase from [here](https://www.couchbase.com/downloads).
After installation, Couchbase will bring up a browser window to complete configuration. Do not use this. Close the window.

## Configuration

Once Couchbase has been installed, various configuration operations are required.  These are executed via the REST interface. You will need curl to carry out these operation.

The following will be configured:

* Console user - Administrator
* Console password - password
* Bucket name - template
* Bucket password - bucketpassword
* Views design document - fortuneEntity
* Data RAM Quota - 4192MB
* Index RAM Quota - 256MB
* Full Text RAM Quota - 512MB
* Index setting - Memory-Optimized Global Secondary Indexes


If you are running Windows, the simplest way to get curl is to install [git for windows](https://git-for-windows.github.io/). This is highly recommended as it will give you
a useful set of utilities in a Linux type shell for the Windows environment.

### Basic Configuration

Setup Services available on the cluster
```
curl -u Administrator:password -v -X POST http://127.0.0.1:8091/node/controller/setupServices -d 'services=kv%2Cn1ql%2Cindex'
```

Initialize storage locations for the node

*Linux*
```
curl -v -X POST http://127.0.0.1:8091/nodes/self/controller/settings -d 'path=C:\couchbase\var\lib\couchbase\data&index_path=C:\couchbase\var\lib\couchbase\idx'
```

*Windows*
```
curl -v -X POST http://127.0.0.1:8091/nodes/self/controller/settings -d 'path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata&index_path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata'
```

Set up your administrator-username and password (replace default)
```
curl -v -X POST http://127.0.0.1:8091/settings/web -d 'password=password&username=Administrator&port=SAME'
```

### Bucket Configuration

Now that Couchbase has been configured, we need to define a bucket to store data into and its associated indexes and views.

Set up a bucket.
```
curl -u Administrator:password -v -X POST http://127.0.0.1:8091/pools/default/buckets -d 'flushEnabled=1&threadsNumber=3&replicaIndex=0&replicaNumber=0&evictionPolicy=valueOnly&ramQuotaMB=1024&bucketType=membase&name=template&authType=sasl&saslPassword=bucketpassword'
```

Set up Memory Optimized Indexes - Need this if indexes are to be used
```
curl -i -u Administrator:password -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'
```

Set up the index RAM quota
```
curl -u Administrator:password -X POST  http://127.0.0.1:8091/pools/default -d 'memoryQuota=4192' -d 'indexMemoryQuota=256'
```

Add views to cluster - these support find() type operations in spring data.
@Views.json is a file containg the views which the command must point to (execute in same directory is simplest). A copy can be found [here](json/views.json)
```
curl -u Administrator:password -X PUT -H 'Content-Type: application/json' http://127.0.0.1:8092/template/_design/fortuneEntity -d @views.json
```

Add a primary index - used to support findAll() operations in spring data
```
curl  -u Administrator:password -v http://localhost:8093/query/service -d 'statement=CREATE PRIMARY INDEX ON template USING GSI'
```

If you now go to the [Coucbase Console](http://localhost:8091/ui/index.html), the configuration can be visually inspected and verified for correctness.

### Other Useful Commands


Get a design document
```
curl -u Administrator:password -X GET  http://127.0.0.1:8092/template/_design/fortuneEntity
```

Set bucket password
```
curl -X POST -u Administrator:password -d 'authType=sasl' -d 'saslPassword=bucketpassword' http://127.0.0.1:8091/pools/default/buckets/template    
```

Delete a bucket
```
curl -X DELETE -u Administrator:password http://127.0.0.1:8091/pools/default/buckets/template
```