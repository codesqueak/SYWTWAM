#Application Metrics

Spring Boot, out of the box, supplies a set of counter and guage metrics with which to monitor the executing application in near real time.

Full details are give in the [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html) documentation.

Most users will find that either these are sufficient for basic monitoring or give a framework where additional metrics can be added.

However, as an example, a process for adding your own independent metrics is detailed below

#Metrics In Spring Boot

To set up a mechanism to handle metrics in the Spring Boot environment, you need to configure a number of components

* Metrics Registry - A container in which to hold all defined metrics.
* Reader - A source of metric data
* Writer - A sink of metrics data
* Exporter - A controller to mediate the flow of information from a Reader to a Writer

##Metrics Registry

The simplest way to build a registry is to extend an existing class without adding further features.

```java
/**
 * Metrics registry to hold custom metrics for the application
 */
public class AppMetricsRegistry extends MetricRegistry {

    public AppMetricsRegistry() {
        super();
    }
}
```

This may be extended with any custom functionality required.

##Reader

Now we have a repository for our metrics, we need to be able to read them.  Fortunately Spring Boot supplies a predefined Reader that can read a registry, so all we need is:

```java
@Bean
public MetricRegistryMetricReader metricReader(final AppMetricsRegistry registry) {
        return new MetricRegistryMetricReader(registry);
    }
```

##Writer

Next, we need a sink for the metrics data.  This is done by implementing the MetricsWriter interface:

```java
/**
 * Example metrics writer - writes to stdout
 */
public class TemplateMetricsWriter implements MetricWriter {

    private final String token;

    public TemplateMetricsWriter(final String name) {
        this.token = name;
    }

    @Override
    public void increment(final Delta<?> delta) {
        System.out.println(token + ":" + delta.getName() + "(increment) " + delta.getValue());
    }

    @Override
    public void reset(final String metricName) {
        System.out.println(token + ":" + "Reset metric: " + metricName);
    }

    @Override
    public void set(final Metric<?> value) {
        System.out.println(token + ":" + value.getName() + "(set) " + value.getValue());
    }
}
```

In a real world scenario, the data output probably would be sent to your application monitoring software of choice, for example, [AppDynamics](https://www.appdynamics.com/), [Datadog](https://www.datadoghq.com/),
[New Relic](https://newrelic.com/), [LogicMonitor](https://www.logicmonitor.com/) amongst many more

It should be noted that most of these (all?) supply pre-build libraries to interface to Spring Boot, thus removing the need to write a MetricsWriter.

##Exporter

The simplest way to obtain an exporter is to use one of the Spring Boot defaults.  The MetricCopyExporter will move data from Reader to Writer every 5 seconds. If a custom exporter is required, 
an AbstractMetricExporter is available which makes a good base on which to build.

```java
    @Bean
    public Exporter exporter(@Qualifier("reader") final MetricRegistryMetricReader reader, @Qualifier("writer") final TemplateMetricsWriter writer) {
        return new MetricCopyExporter(reader, writer);
    }
    
```
#DropWizard

A large set of pre-built metrics are available via [DropWizard](http://www.dropwizard.io)

These can be easily used by adding them to a registry.

```java
       register("jvm.memory", new MemoryUsageGaugeSet());
       register("jvm.garbage-collector", new GarbageCollectorMetricSet());
```

#Gotchas

Things to look out for:

* The MetricsCopyExporter ties to optimize output (Filters out non changing values).  However, the Dropwizard MetricRegistry has no support for timestamps, so the optimization is not 
available if you are using Dropwizard metrics 

* While using Spring Boot metrics is a very easy way to generate information, be careful not to leak sensitive information

* Use Java 8 if possible, as the metrics are a lot faster due to use of atomic in-memory buffers

* The default metrics are visible via http://localhost:<port>/metrics
















