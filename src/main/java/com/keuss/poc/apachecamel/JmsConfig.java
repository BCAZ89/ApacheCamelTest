package com.keuss.poc.apachecamel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
public class JmsConfig {

    // setup JMS connection factory
    @Bean(destroyMethod = "stop", initMethod = "start")
    PooledConnectionFactory poolConnectionFactory(final ActiveMQConnectionFactory jmsConnectionFactory) {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(jmsConnectionFactory);
        pooledConnectionFactory.setMaxConnections(8);
        return pooledConnectionFactory;
    }

    @Bean
    ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
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
        return activeMQComponent;
    }

}
