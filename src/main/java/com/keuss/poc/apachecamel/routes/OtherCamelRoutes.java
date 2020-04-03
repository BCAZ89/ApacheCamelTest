package com.keuss.poc.apachecamel.routes;

import com.keuss.poc.apachecamel.exceptions.CustomException;
import com.keuss.poc.apachecamel.pojos.Book;
import com.keuss.poc.apachecamel.services.MyService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class OtherCamelRoutes extends RouteBuilder {

    // Fro JSON array
    JacksonDataFormat format = new ListJacksonDataFormat(Book.class);

    @Override
    public void configure() throws Exception {

        // global (for Java DSL that is per RouteBuilder instances)
        // can add multiple exceptions
        onException(CustomException.class)
                .maximumRedeliveries(3)
                .to("activemq:queue.error")
                .log("error sent back to the client");

        from("activemq:queue.testggal3")
                .bean(MyService.class, "doSomething(${body}, ${headers}, ${headers.JMSCorrelationID})")
                .log("Camel body: ${body}");

        // see https://stackoverflow.com/questions/50732754/configuring-datasource-for-apache-camel
        // and https://camel.apache.org/components/latest/jdbc-component.html
        // not need to register our datasource in the Camel registry ?
        from("timer:mytimer2?period=10000&delay=25000&repeatCount=3")
                .setBody(constant("SELECT * FROM BOOK"))
                .to("jdbc:dataSource")
                .split(body())
                .bean(MyService.class, "doSomethingBis(${body})")
                .log("Camel body from JDBC and bean: ${body}");


        // JSON https://camel.apache.org/manual/latest/json.html
        // and https://stackoverflow.com/questions/40756027/apache-camel-json-marshalling-to-pojo-java-bean
        // and https://stackoverflow.com/questions/46411214/how-to-unmarshal-json-body-to-list-of-myclass-in-camel/46411822
        // with JSON : {"id": 1, "title": "LOTR 1", "author": "toto"}
        from("activemq:queue.testggal4")
                .unmarshal().json(JsonLibrary.Jackson, Book.class)
                .bean(MyService.class, "doSomethingJson(${body})")
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
        from("activemq:queue.testggal5")
                .unmarshal(format)
                .bean(MyService.class, "doSomethingJsonList(${body})")
                .log("Camel body unmarshal list and bean: ${body}");
    }
}
