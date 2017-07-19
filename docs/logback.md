# Logback

Logging for the project is handled by Logback. [Logback](https://logback.qos.ch/) is intended as a successor to [log4j](https://logging.apache.org/log4j/2.x/) project, so will be
generally familiar to in operation to most Java developers.

# Spring Boot Support for Logback

With Spring Boot, implementing logging support is a relatively simple process as shown [here](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html)

The configuration for the project is defined in _/resources/logback-spring.xml_.   The configuration defines three logging appenders:

1. CONSOLE - Console logging formatted for readability.  Uses ANSI Color-coded output. Useful for debugging, but remove for production
2. FILE - File logging formatted as standard one line per log record.  Useful for debugging, but remove for production
3. JSON - File logging formatted using JSON.  This is defined for feeding into [Logstash](https://www.elastic.co/products/logstash) for processing in an [ELK](https://www.elastic.co/products) environment.

Logging uses System Properties as it needs to be available before the Application Context is available.  
As part of startup, a limited number of properties are copied from the `bootstrap.properties` to System Properties.  These are:

| Spring Property| System Property |
|----------------|-----------------|
|logging.exception-conversion-word | LOG_EXCEPTION_CONVERSION_WORD
|logging.file | LOG_FILE
|logging.path | LOG_PATH
|logging.pattern.console | CONSOLE_LOG_PATTERN
|logging.pattern.file | FILE_LOG_PATTERN
|logging.pattern.level | LOG_LEVEL_PATTERN
|PID | PID

Entries defined in `bootstrap.properties`
```
# Logging
logging.path=log
logging.file=sywtwam
```

## CONSOLE Logging

The console logging configuration is defined as:

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        <charset>utf8</charset>
    </encoder>
</appender>
```

This uses the standard console appender to give the ANSI Color-coded output

## FILE Logging

The file logging appender has two interesting features:

```xml
<property name="LOGGER_FILE" value="${LOG_PATH}/${LOG_FILE}.log"/>

<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <encoder>
        <pattern>${FILE_LOG_PATTERN}</pattern>
        <charset>utf8</charset>
    </encoder>
    <file>${LOGGER_FILE}</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
        <fileNamePattern>${LOGGER_FILE}.%i</fileNamePattern>
        <minIndex>1</minIndex>
        <maxIndex>10</maxIndex>
    </rollingPolicy>
    <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
        <MaxFileSize>10MB</MaxFileSize>
    </triggeringPolicy>
</appender>
```

1. `FixedWindowRollingPolicy` - This defined the strategy for log file naming and retention. In this case they are <LOG_FILE>.log.n, where n=index. 
Retention here is defined to 10 files with the oldest being deleted as required.
2. `SizeBasedTriggeringPolicy` - This defines the size of a log file has to reach before rolling over to the next file


## JSON Logging

JSON logging is designed to feed Logstash and is not intended to be used directly.  Have a look at CONSOLE or FILE if you need to see what is happening.

The appender uses `FixedWindowRollingPolicy` and `SizeBasedTriggeringPolicy` in a similar manner to the FILE appender to control file size and rollover.

The information to be encoded into JSON is controller by the `LoggingEventCompositeJsonEncoder` which uses a set of `providers` to define fields.  Full usage and configuration details are
defined [here](https://github.com/logstash/logstash-logback-encoder) in the Logstash documentation.

The full appender is defined as:

```xml
<appender name="JSON" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <File>${JSON_FILE}</File>
    <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
        <providers>
            <logLevel/>
            <threadName/>
            <pattern>
                <pattern>
                    {
                    "timestamp": "%date{yyyy-MM-dd'T'HH:mm:ss.SSSZZ}",
                    "PID": "${PID}",
                    "system": "${SYSTEM}",
                    "subsystem": "${SUBSYSTEM}",
                    "class": "%class{60}"
                    }
                </pattern>
            </pattern>
            <message/>
        </providers>
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
        <fileNamePattern>${JSON_FILE}.%i</fileNamePattern>
        <minIndex>1</minIndex>
        <maxIndex>10</maxIndex>
    </rollingPolicy>
    <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
        <MaxFileSize>10MB</MaxFileSize>
    </triggeringPolicy>
</appender>
```

... and this gives JSON records such as:

```json
{
  "level": "INFO",
  "thread_name": "main",
  "timestamp": "2017-07-20T00:11:22.040+0100",
  "PID": "8644",
  "system": "Template",
  "subsystem": "Spring",
  "class": "o.s.integration.monitor.IntegrationMBeanExporter",
  "message": "Registering MessageChannel errorChannel"
}
```

## Other Delivery Methods

It is possible to deliver log records over a network which bypasses the use of the file system completely. This may be done via UDP or TCP using
the `LogstashSocketAppender` or `LogstashTcpSocketAppender` appender. A disadvantage of this is the possibility of log loss due to network issues.  

## A Note on Timestamps

It is important to have consistent timestamp information from Logback through Logstash and through to Elasticsearch.  Failure to do this will lead to missing log records. The simplest way
to ensure correct handling is to decide on a format and specify this wherever timestamp information is handled. In this instance, the following format was chosen:
```
yyyy-MM-dd'T'HH:mm:ss.SSSZZ
```
Which gives timestamps such as: _2017-07-20T00:11:23.320+0100_






