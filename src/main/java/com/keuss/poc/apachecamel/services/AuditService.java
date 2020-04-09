package com.keuss.poc.apachecamel.services;

import org.apache.camel.Message;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void audit(Message message) {
        System.out.println("AuditService body " + message.getBody());
    }
}
