package com.keuss.poc.apachecamel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        System.out.println("payload: " + payload);
        // do something with the payload and/or exchange here
        exchange.getIn().setBody("Changed body at " + LocalDateTime.now());
    }
}