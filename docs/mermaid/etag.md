# If-Match Logic
````
graph TD;
    If-Match((If-Match Present))-->evaluateIfMatch
    evaluateIfMatch{Evaluate If-Match}--false-->return412
    evaluateIfMatch--true-->try
    return412((Precondition Failed 412))
    try{If-None-Match Present}--true-->exit
    try--false-->execute((Execute Request))
    exit((Try If-None-Match))
````
# If-None_match Logic
````
graph TD;
    If-Not-Match((If-None-Match Present))-->evaluate
    evaluate{Evaluate If-None-Match}--true-->execute
    evaluate--false-->gethead
    gethead{Is GET or HEAD}--true-->return304
    gethead--false-->return412
    return412((Precondition Failed 412))
    return304((Not Modified 304))
    execute((Execute Request))
````

