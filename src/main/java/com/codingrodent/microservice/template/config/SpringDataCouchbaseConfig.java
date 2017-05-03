package com.codingrodent.microservice.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

/**
 * Example couchbase configuration data pulled from application.properties
 */
//@Profile("prod")
@Configuration
@EnableCouchbaseRepositories(basePackages = "com.codingrodent.microservice.template.repository")
public class SpringDataCouchbaseConfig {}
