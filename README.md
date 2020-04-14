# ApacheCamelTest

Apache Camel and Spring Boot starter to startup the Camel Context (`org.apache.camel.springboot`)

 - Java 8
 - [Apache Camel](https://camel.apache.org/) 3.2.0 with java DSL
 - [Spring Boot](https://spring.io/projects/spring-boot) 2.2.6.RELEASE
 - [Apache ActiveMQ](https://activemq.apache.org/) 5.15.12
 - Also with `spring-boot-starter-web` for Rest Controller and `spring-boot-starter-jdbc`
 - H2 DB
 - Jackson for JSON unmarshal and marshal
 
# Build and Run

 - `git clone https://github.com/keuss/ApacheCamelTest.git`
 - `mvn clean package`
 - `activemq start` (see https://activemq.apache.org/components/classic/download/)
 - With 5 ActiveMQ queues : `queue.testggal, queue.testggal2, queue.testggal3, queue.testggal4, queue.testggal5, queue.error`
 - `java -jar target/SpringBootAndApacheCamel-0.0.1-SNAPSHOT.jar`
 - ActiveMQ administrative [interface](http://127.0.0.1:8161/admin/) (admin/admin)
 - [H2 console](http://localhost:8080/api/h2-console/) with `jdbc:h2:mem:testdb` (see [my_schema.sql](https://github.com/keuss/ApacheCamelTest/blob/master/src/main/resources/db/my_schema.sql))

# Camel [Components](https://camel.apache.org/components/latest/index.html)

 - direct component
 - timer component
 - activemq component
 - bean component
 - jdbc component
 - seda component
 
# Enterprise Integration Patterns [EPI](https://camel.apache.org/components/latest/eips/enterprise-integration-patterns.html)

 - Point to Point Channel
 - log
 - to
 - bean
 - marshal & unmarshal
 - split
 - Dead Letter Channel
 - WireTap
 - Competing Consumers with SEDA and JMS concurrentConsumers)
