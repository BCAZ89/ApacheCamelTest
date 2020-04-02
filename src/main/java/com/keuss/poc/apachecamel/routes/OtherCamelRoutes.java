package com.keuss.poc.apachecamel.routes;

import com.keuss.poc.apachecamel.services.MyService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OtherCamelRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue.testggal3")
                .bean(MyService.class, "doSomething(${body}, ${headers}, ${headers.JMSCorrelationID})")
                .log("Camel body: ${body}");

        // see https://stackoverflow.com/questions/50732754/configuring-datasource-for-apache-camel
        // and https://camel.apache.org/components/latest/jdbc-component.html
        // not need to register our datasource in the Camel registry ?
        from("timer:mytimer2?period=10000&delay=15000&repeatCount=3")
                .setBody(constant("SELECT * FROM BOOK"))
                .to("jdbc:dataSource")
                .split(body())
                .log("Camel body from JDBC: ${body}");
    }
}
