package com.keuss.poc.apachecamel.routes;

import com.keuss.poc.apachecamel.exceptions.CustomRuntimeException;
import com.keuss.poc.apachecamel.pojos.Book;
import com.keuss.poc.apachecamel.processors.MyPrepareProcessor;
import com.keuss.poc.apachecamel.processors.MyProcessorError;
import com.keuss.poc.apachecamel.services.MyService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtherCamelRoutes extends RouteBuilder {

    // Fro JSON array
    JacksonDataFormat format = new ListJacksonDataFormat(Book.class);

    private final MyProcessorError myProcessorError;

    private final MyPrepareProcessor myPrepareProcessor;

    private final MyService myService;

    @Override
    public void configure() throws Exception {

        // global (for Java DSL that is per RouteBuilder instances)
        // will use original message (body and headers)
        // can add multiple exceptions, then errorHandler
        onException(CustomRuntimeException.class)
                .useOriginalMessage()
                .maximumRedeliveries(5).redeliveryDelay(1000);

        // will use original message (body and headers)
        // see https://camel.apache.org/components/latest/eips/dead-letter-channel.html
        // and https://camel.apache.org/manual/latest/error-handler.html
        // When Dead Letter Channel is doing redeliver its possible to configure a Processor that is executed just before
        // every redelivery attempt. This can be used for the situations where you need to alter the message before its redelivered
        // Before the exchange is sent to the dead letter queue, you can use onPrepare to allow a custom Processor
        // to prepare the exchange, such as adding information why the Exchange failed.
        errorHandler(deadLetterChannel("{{input.dlq.name}}")
                .onRedelivery(myProcessorError)
                .onPrepareFailure(myPrepareProcessor)
                .useOriginalMessage().maximumRedeliveries(3).redeliveryDelay(2000));

        from("activemq:queue.testggal3")
                .bean(myService, "doSomething(${body}, ${headers}, ${headers.JMSCorrelationID})")
                .log("Camel body: ${body}");

        // see https://stackoverflow.com/questions/50732754/configuring-datasource-for-apache-camel
        // and https://camel.apache.org/components/latest/jdbc-component.html
        // not need to register our datasource in the Camel registry ?
        from("timer:mytimer2?period=10000&delay=25000&repeatCount=3")
                .setBody(constant("SELECT * FROM BOOK"))
                .to("jdbc:dataSource")
                .split(body())
                .bean(myService, "doSomethingBis(${body})")
                .log("Camel body from JDBC and bean: ${body}");


        // JSON https://camel.apache.org/manual/latest/json.html
        // and https://stackoverflow.com/questions/40756027/apache-camel-json-marshalling-to-pojo-java-bean
        // and https://stackoverflow.com/questions/46411214/how-to-unmarshal-json-body-to-list-of-myclass-in-camel/46411822
        // with JSON : {"id": 1, "title": "LOTR 1", "author": "toto"}
        from("activemq:queue.testggal4")
                .wireTap("seda:audit")
                .unmarshal().json(JsonLibrary.Jackson, Book.class)
                .bean(myService, "doSomethingJson(${body})")
                .log("Camel body unmarshal and bean: ${body}");

        // with JSON :
        /*
            [{
            "id": 1,
            "title": "LOTR 1",
            "author": "toto"
            },
            {
                "id": 2,
                "title": "LOTR 2",
                "author": "toto"
            }
            ]
         */
        // Call another route with direct
        // https://camel.apache.org/components/latest/eips/split-eip.html
        from("activemq:queue.testggal5")
                .unmarshal(format)
                .bean(myService, "doSomethingJsonList(${body})")
                .split(body())
                .marshal().json()
                .to("direct:customLog");


        from("activemq:queue.testggal6?concurrentConsumers=5")
                .process(exchange -> {
                    System.out.println(Thread.currentThread() + " - " + exchange.getIn().getBody());
                    Thread.sleep(5000);
                });

    }
}
