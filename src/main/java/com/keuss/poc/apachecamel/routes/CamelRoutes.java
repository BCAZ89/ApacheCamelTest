package com.keuss.poc.apachecamel.routes;

import com.keuss.poc.apachecamel.processors.MyProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CamelRoutes extends RouteBuilder {

    private final MyProcessor myProcessor;

    @Override
    public void configure() throws Exception {

        // Then these routes will be started automatically.
        // You can customize the Camel application in the application.properties

        // With Apache ActiveMQ 5.15.12 (to start broker : activemq start)
        // ActiveMQ administrative interface at http://127.0.0.1:8161/admin/ (admin/admin)
        // Broker at tcp://localhost:61616

        // https://camel.apache.org/manual/latest/dsl.html
        // JAVA DSL HERE https://camel.apache.org/manual/latest/java-dsl.html
        // OR Camel Annotation DSL  https://camel.apache.org/manual/latest/bean-integration.html

        // direct component
        // https://camel.apache.org/components/latest/direct-component.html
        // Here log directly in the DSL (not the component)
        from("direct:firstRoute")
                .log("Camel body: ${body}");

        // timer, activemq components
        // https://camel.apache.org/components/latest/timer-component.html
        // https://camel.apache.org/components/latest/activemq-component.html
        // queue name is "queue.testggal"
        // https://camel.apache.org/components/latest/languages/simple-language.html
        from("timer:mytimer?period=5000&delay=10000&repeatCount=3")
                .setBody(simple("Hello from timer at ${header.firedTime}"))
                .to("activemq:queue.testggal");

        // activemq component
        // https://camel.apache.org/manual/latest/processor.html
        from("activemq:queue.testggal")
                .process(myProcessor)
                .log("Camel body from jms: ${body}")
                .to("activemq:queue.testggal2");
    }
}
