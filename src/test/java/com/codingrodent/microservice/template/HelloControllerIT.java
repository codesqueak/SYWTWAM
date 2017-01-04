package com.codingrodent.microservice.template;

import org.junit.Before;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Inject
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    // @Test
    public void getHello() {
        ResponseEntity<String> response = template.getForEntity(base.toString() + "/hello", String.class);
        assertThat(response.getBody(), equalTo("{\"msg\":\"Greetings from Spring Boot!\"}"));
    }
}
