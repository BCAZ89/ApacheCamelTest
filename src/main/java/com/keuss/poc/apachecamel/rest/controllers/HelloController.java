package com.keuss.poc.apachecamel.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "hello", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HelloController {

    private final Random rand = new Random();

    private final CamelContext camelContext;

    // see https://camel.apache.org/manual/latest/producertemplate.html
    private final ProducerTemplate producerTemplate;

    @GetMapping(value = "{name}")
    public String hello(@PathVariable("name") String name) {
        // TEST with http://localhost:8080/api/hello/keuss
        return "{\"data\": \"Hello " + name + "\"}";
    }

    @GetMapping(value = "camel/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloCamel(@PathVariable("name") String name) {
        // TEST with http://localhost:8080/api/hello/camel/keuss
        // see https://camel.apache.org/manual/latest/producertemplate.html#ProducerTemplate-requestmethods
        return producerTemplate.requestBody("direct:firstRoute",
                "Calling via Spring Boot Rest Controller with " + name, String.class);
    }

    @GetMapping(value = "camel/jms/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloCamelJms(@PathVariable("name") String name) {
        // TEST with http://localhost:8080/api/hello/camel/jms/keuss
        // see https://camel.apache.org/manual/latest/producertemplate.html#ProducerTemplate-requestmethods
        return producerTemplate.requestBody("activemq:queue.testggal3",
                "Calling via Spring Boot Rest Controller with " + name, String.class);
    }

    private int randomNum(int max, int min) {
        return rand.nextInt((max - min) + 1) + min;
    }

    @GetMapping(value = "camelgroups")
    public void helloCamelMessageGroups() {
        // TEST with http://localhost:8080/api/hello/camelgroups
        int randomNum;
        for (int i = 0; i < 1000; ++i) {
            randomNum = randomNum(4, 1);
            camelContext.createFluentProducerTemplate()
                    .withBody("This is a message from group : " + randomNum)
                    .withHeader("JMSXGroupID", "" + randomNum)
                    .to("activemq:queue.testggal6")
                    .send();
        }
    }

}