package com.keuss.poc.apachecamel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
@PropertySource(value = "classpath:my-application.properties")
public class JmsConfig {

    @Value("${custom.broker.url}")
    private String borkerUrl;

    // setup JMS connection factory
    // For efficient pooling of the connections and sessions for your collection of consumers
    @Bean(destroyMethod = "stop", initMethod = "start")
    PooledConnectionFactory poolConnectionFactory(final ActiveMQConnectionFactory jmsConnectionFactory) {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(jmsConnectionFactory);
        // see this in http://127.0.0.1:8161/admin/connections.jsp
        // default value is 1 but 5 with spring configuration in properties
        pooledConnectionFactory.setMaxConnections(8);
        return pooledConnectionFactory;
    }

    @Bean
    ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(borkerUrl);
        return activeMQConnectionFactory;
    }

    // setup spring jms TX manager
    @Bean
    public JmsTransactionManager jmsTransactionManager(final PooledConnectionFactory poolConnectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(poolConnectionFactory);
        return jmsTransactionManager;
    }

    // define our activemq component
    @Bean(name = "activemq")
    public JmsComponent jmsComponent(final PooledConnectionFactory poolConnectionFactory,
                                     final JmsTransactionManager jmsTransactionManager) {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(poolConnectionFactory);
        activeMQComponent.setTransactionManager(jmsTransactionManager);
        activeMQComponent.setUsePooledConnection(true);
        activeMQComponent.setTransacted(true);
        activeMQComponent.setLazyCreateTransactionManager(false);
        activeMQComponent.setCacheLevelName("CACHE_CONSUMER");
        activeMQComponent.setAcknowledgementModeName("SESSION_TRANSACTED");
        // May be tuning with setConcurrentConsumers !
        // see concurrentConsumers https://camel.apache.org/components/latest/activemq-component.html (default is 1)
        // see 'Number Of Consumers' from http://127.0.0.1:8161/admin/queues.jsp
        activeMQComponent.setConcurrentConsumers(4);
        return activeMQComponent;
    }

}
