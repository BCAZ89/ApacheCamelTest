package com.keuss.poc.apachecamel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {

        // Processor is a simple Java interface which is used to add custom integration logic to a route. It contains a
        // single process method used to preform custom business logic on a message received by a consumer

        // Exchange is the container of a message and it is created when a message is received by a consumer during
        // the routing process. Exchange allows different types of interaction between systems – it can define
        // a one-way message or a request-response message

        String payload = exchange.getIn().getBody(String.class);
        System.out.println("payload: " + payload);
        // do something with the payload and/or exchange here
        exchange.getIn().setBody("Changed body at " + LocalDateTime.now());
    }
}