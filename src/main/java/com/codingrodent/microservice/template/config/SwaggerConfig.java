package com.codingrodent.microservice.template.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Configure the springfox REST JSON API documentation tool
 */
@Configuration
@EnableSwagger2
@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class, springfox.bean.validators.configuration
        .BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    @Inject
    public SwaggerConfig(final TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    /**
     * Define the Swagger docket for the Template REST API
     *
     * @return Template swagger configuration
     */
    @Bean
    public Docket panopticonApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // Unique docklet name - only required if more than one present
                .groupName("template-api")
                // Say where the endpoints are to be discovered
                .select().apis(RequestHandlerSelectors.basePackage("com.codingrodent.microservice.template")).build()
                // Don't use default HTTP response code - we will define our responses
                .useDefaultResponseMessages(false)
                // Sets information to be displayed in the API resource listing
                .apiInfo(apiInfo())
                // Tags used to identify components - purely documentation
                .tags(new Tag("sync", "Synch demo interface"), new Tag("async", "Asynch demo interface"))
                // Model substitution rule
                .directModelSubstitute(LocalDate.class, String.class)
                //
                // A more complex substitution rule
                // The type resolver is resolving DeferredResult<ResponseEntity<?>> to ?
                // The rule  maps DeferredResult<ResponseEntity<?>> to ? for the model
                .alternateTypeRules(newRule(//
                        typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)), //
                        typeResolver.resolve(WildcardType.class)))
                //
                //  	Sets up the security schemes used to protect the apis. Can be ApiKey, BasicAuth and OAuth -- not used at the moment
                .securitySchemes(Collections.singletonList(new ApiKey("ApiKey", "api_key", "header")));
        //
        // Add HTTP responses - this is a default set that is overridden later as required
        addHTTPResponses(docket);
        return docket;
    }

    /**
     * Product API information
     *
     * @return Product API information Swagger bean
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Template Core Server API", //
                "Standard REST API template", //
                "0.1", //
                null, //
                new Contact("admin", "https://github.com/codesqueak/SYWTWAM", "codesqueak@gmail.com"), //
                "The MIT License (MIT)", //
                "https://opensource.org/licenses/MIT");
    }

    /**
     * General UI configuration customization - This is a generally acceptable default
     *
     * @return Custom UI configuration
     */
    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null,// url - switch off validation
                "none",       // docExpansion          => none | list
                "alpha",      // apiSorter             => alpha
                "schema",     // defaultModelRendering => schema
                new String[]{"get", "post", "put", "delete", "patch", "head", "options"}, // add head and options as not set by default
                false,        // enableJsonEditor      => true | false
                true,         // showRequestHeaders    => true | false
                60000L);      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
    }

    /**
     * Add all defined response messages to their associated request methods
     *
     * @param docket Swagger Docket being populated
     */
    private void addHTTPResponses(Docket docket) {
        HashMap<RequestMethod, LinkedList<ResponseMessage>> responseMessages = new HashMap<>();
        for (RequestMethod requestMethod : RequestMethod.values()) {
            responseMessages.put(requestMethod, new LinkedList<>());
        }
        //
        // Add response messages - globals first
        //
        // 400
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "The request cannot be fulfilled due to bad syntax", null,
                Collections.emptyMap(), Collections.emptyList()));
        //
        // 401
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "The request requires user authentication", null, Collections
                .emptyMap(), Collections.emptyList()));
        // 403
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.FORBIDDEN.value(), "User not authorized to perform the operation or the resource is " + "" + "" + "" + "" + "" + "" + "" + "" + "" + "unavailable", null, Collections.emptyMap(), Collections.emptyList()));
        //
        // 404
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Resource not found", null, Collections.emptyMap(), Collections
                .emptyList()));
        //
        // 405
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not allowed on resource", null, Collections
                .emptyMap(), Collections.emptyList()));
        //
        // 500
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A server fault has occurred", null, Collections
                .emptyMap(), Collections.emptyList()));
        //
        // 501
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.NOT_IMPLEMENTED.value(), "Requested HTTP operation not supported", null, Collections
                .emptyMap(), Collections.emptyList()));
        //
        // 503
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), "The service is unavailable", null, Collections
                .emptyMap(), Collections.emptyList()));
        //
        // ... and now by request methods
        //
        // 200
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.OK.value(), "Resource returned without error", null, Collections.emptyMap(),
                Collections.emptyList()), RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.TRACE);
        //
        // 201
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.CREATED.value(), "Resource created without error", null, Collections.emptyMap(),
                Collections.emptyList()), RequestMethod.POST, RequestMethod.PUT);
        //
        // 304
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), "Not modified", null, Collections.emptyMap(), Collections
                .emptyList()), RequestMethod.GET);
        //
        //
        // 409
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.CONFLICT.value(), "Conflict", null, Collections.emptyMap(), Collections.emptyList())
                , RequestMethod.PUT);
        //
        // 418
        addHttpResponse(responseMessages, new ResponseMessage(HttpStatus.I_AM_A_TEAPOT.value(), "Hyper Text Coffee Pot Control Protocol", null, Collections
                .emptyMap(), Collections.emptyList()), RequestMethod.TRACE);

        //
        // update docket
        for (RequestMethod requestMethod : RequestMethod.values()) {
            docket.globalResponseMessage(requestMethod, responseMessages.get(requestMethod));
        }
    }

    /**
     * Add response messages to one or more request methods
     *
     * @param responseMessages Map of request methods to response messages
     * @param responseMessage  The response message to add
     * @param requestMethods   Request methods to add response messages to. If empty then the response message is
     *                         added to ALL
     *                         possible request methods.
     */
    private void addHttpResponse(HashMap<RequestMethod, LinkedList<ResponseMessage>> responseMessages, ResponseMessage responseMessage, RequestMethod...
            requestMethods) {
        if (0 == requestMethods.length) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            responseMessages.get(requestMethod).add(responseMessage);
        }
    }

}
