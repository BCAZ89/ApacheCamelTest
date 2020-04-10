package com.keuss.poc.apachecamel.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hello", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HelloController {

    @Autowired
    private CamelContext camelContext;

    private final ProducerTemplate producerTemplate;

    @GetMapping(value = "{name}")
    public String hello(@PathVariable("name") String name) {
        // TEST with http://localhost:8080/api/hello/keuss
        return "{\"data\": \"Hello " + name + "\"}";
    }

    @GetMapping(value = "camel/{name}")
    public void helloCamel(@PathVariable("name") String name) {
        // TEST with http://localhost:8080/api/hello/camel/keuss
        producerTemplate.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller with " + name);
    }

    @GetMapping(value = "camelgroups")
    public void helloCamelMessageGroups() {
        // TEST with http://localhost:8080/api/hello/camelgroups
        for (int i = 0; i < 1000; ++i) {
            camelContext.createFluentProducerTemplate()
                    .withBody("This is a message from group : " + (i % 5))
                    .withHeader("JMSXGroupID", "" + (i % 5))
                    .to("activemq:queue.testggal6")
                    .send();
        }
    }

}