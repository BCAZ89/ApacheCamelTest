package com.keuss.poc.apachecamel.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MyService {
    public String doSomething(String body, Map headers, String jmsCorrelationIDparam) {
        // process the in body and return whatever you want
        System.out.println("MyService body " + body);
        System.out.println("MyService jmsCorrelationIDparam " + jmsCorrelationIDparam);
        headers.forEach((k, v) -> System.out.println(k + ":" + v));
        return "Bye World";
    }
}
