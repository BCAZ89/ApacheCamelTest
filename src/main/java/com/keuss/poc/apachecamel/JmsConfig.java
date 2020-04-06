package com.keuss.poc.apachecamel;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsConfig {

    @Bean
    ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager(final ConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(connectionFactory);
        return jmsTransactionManager;
    }

    @Bean(name = "activemq")
    public JmsComponent jmsComponent(final ConnectionFactory connectionFactory,
                                     final JmsTransactionManager jmsTransactionManager) {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(connectionFactory);
        activeMQComponent.setTransactionManager(jmsTransactionManager);
        activeMQComponent.setTransacted(true);
        activeMQComponent.setLazyCreateTransactionManager(false);
        activeMQComponent.setCacheLevelName("CACHE_CONSUMER");
        activeMQComponent.setAcknowledgementModeName("SESSION_TRANSACTED");
        return activeMQComponent;
    }

    //add pooled cf !
}
