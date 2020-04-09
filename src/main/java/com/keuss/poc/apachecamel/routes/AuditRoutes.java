package com.keuss.poc.apachecamel.routes;


import com.keuss.poc.apachecamel.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditRoutes extends RouteBuilder {

    private final AuditService auditService;

    @Override
    public void configure() throws Exception {
        from("seda:audit")
                .bean(auditService, "audit");
    }
}
