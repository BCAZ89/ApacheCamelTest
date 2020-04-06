package com.keuss.poc.apachecamel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class MyProcessorError implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        System.out.println("payload: " + payload);
        System.out.println("exception: " + exception.getMessage());
    }
}
