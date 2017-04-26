# Couchbase

The example code uses spring-data-couchbase as its respository handler.  To support this, couchbase needs to be installed and configured.

## Installation

## Configuration

Once couchbase has been installed, various configuration operations are required.  These are executed via the REST interface. You will need curl to carry out these operation.

### Basic Configuration

// Setup Servies available on the cluster
```
curl -u Administrator:password -v -X POST http://127.0.0.1:8091/node/controller/setupServices -d 'services=kv%2Cn1ql%2Cindex'
```

Initialize Node - Note use of Windows paths. Change for linux.
```
curl -v -X POST http://127.0.0.1:8091/nodes/self/controller/settings -d 'path=C:\couchbase\var\lib\couchbase\data&index_path=C:\couchbase\var\lib\couchbase\idx'
```

Set up your administrator-username and password (replace default)
```
curl -v -X POST http://127.0.0.1:8091/settings/web -d 'password=password&username=Administrator&port=SAME'

```

### Bucket Configuration

Now that couchbase has been configured, we need to define a bucket to store data into

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
@Views.json is a file containg the views which the command must point to (execute in same directory is simplest)
```
curl -u Administrator:password -X PUT -H 'Content-Type: application/json' http://127.0.0.1:8092/template/_design/fortuneEntity -d @views.json
```

Add a primary index - used to support findAll() operations in spring data
```
curl  -u Administrator:password -v http://localhost:8093/query/service -d 'statement=CREATE PRIMARY INDEX ON template USING GSI'
```

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