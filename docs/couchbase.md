# Couchbase


// Setup Services
curl -u Administrator:password -v -X POST http://127.0.0.1:8091/node/controller/setupServices -d 'services=kv%2Cn1ql%2Cindex'


// Initialize Node


curl -v -X POST http://127.0.0.1:8091/nodes/self/controller/settings -d 'path=C:\couchbase\var\lib\couchbase\data&index_path=C:\couchbase\var\lib\couchbase\idx'



// Set up your administrator-username and password (replace default)

curl -v -X POST http://127.0.0.1:8091/settings/web -d 'password=password&username=Administrator&port=SAME'


// Set up a bucket.
curl -u Administrator:password -v -X POST http://127.0.0.1:8091/pools/default/buckets -d 'flushEnabled=1&threadsNumber=3&replicaIndex=0&replicaNumber=0&evictionPolicy=valueOnly&ramQuotaMB=1024&bucketType=membase&name=template&authType=sasl&saslPassword=bucketpassword'



# Setup Memory Optimized Indexes
curl -i -u Administrator:password -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'


// Set up the index RAM quota (to be applied across the entire cluster).

curl -u Administrator:password -X POST  http://127.0.0.1:8091/pools/default -d 'memoryQuota=4192' -d 'indexMemoryQuota=256' 


// all done :)

// Add views

curl -u Administrator:password -X PUT -H 'Content-Type: application/json' http://127.0.0.1:8092/template/_design/fortuneEntity -d @views.json

// primary index

curl  -u Administrator:password -v http://localhost:8093/query/service -d 'statement=CREATE PRIMARY INDEX ON template USING GSI'





// get a design document

curl -u Administrator:password -X GET  http://127.0.0.1:8092/template/_design/fortuneEntity

// set bucket password


curl -X POST -u Administrator:password -d 'authType=sasl' -d 'saslPassword=bucketpassword' http://127.0.0.1:8091/pools/default/buckets/template    

// delete a bucket

curl -X DELETE -u Administrator:password http://127.0.0.1:8091/pools/default/buckets/template
