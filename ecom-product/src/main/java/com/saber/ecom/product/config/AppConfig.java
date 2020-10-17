package com.saber.ecom.product.config;

import org.axonframework.eventhandling.*;
import org.axonframework.eventhandling.amqp.DefaultAMQPMessageConverter;
import org.axonframework.eventhandling.amqp.spring.ListenerContainerLifecycleManager;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPTerminal;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class AppConfig {

    @Value("${ecom.amqp.rabbit.address}")
    private String rabbitMQAddress;

    @Value("${ecom.amqp.rabbit.username}")
    private String rabbitMQUser;

    @Value("${ecom.amqp.rabbit.password}")
    private String rabbitMQPassword;

    @Value("${ecom.amqp.rabbit.vhost}")
    private String rabbitMQVhost;

    @Value("${ecom.amqp.rabbit.exchange}")
    private String rabbitMQExchange;

    @Value("${ecom.amqp.rabbit.queue}")
    private String rabbitMQQueue;


    @Bean
    public XStreamSerializer xStreamSerializer() {
        return new XStreamSerializer();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQAddress);
        connectionFactory.setUsername(rabbitMQUser);
        connectionFactory.setPassword(rabbitMQPassword);
        connectionFactory.setVirtualHost(rabbitMQVhost);
        connectionFactory.setConnectionTimeout(500_000);
        connectionFactory.setRequestedHeartBeat(20);
        return connectionFactory;
    }

    @Bean
    public FanoutExchange eventBusExchange() {
        return new FanoutExchange(rabbitMQExchange, true, false);
    }

    @Bean
    public Queue eventBusQueue() {
        return new Queue(rabbitMQQueue, true, false, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(eventBusQueue()).to(eventBusExchange());
    }


    @Bean
    public SimpleCluster simpleCluster() {
        return new SimpleCluster(rabbitMQQueue);
    }

    @Bean
    public DefaultAMQPMessageConverter defaultAMQPMessageConverter() {
        return new DefaultAMQPMessageConverter(xStreamSerializer());
    }

    @Bean
    public ListenerContainerLifecycleManager listenerContainerLifecycleManager() {
        ListenerContainerLifecycleManager listenerContainerLifecycleManager = new ListenerContainerLifecycleManager();
        listenerContainerLifecycleManager.setConnectionFactory(connectionFactory());
        return listenerContainerLifecycleManager;
    }

    @Bean
    public EventBusTerminal terminal() {
        SpringAMQPTerminal terminal = new SpringAMQPTerminal();
        terminal.setConnectionFactory(connectionFactory());
        terminal.setSerializer(xStreamSerializer());
        terminal.setExchangeName(rabbitMQExchange);
        terminal.setListenerContainerLifecycleManager(listenerContainerLifecycleManager());
        terminal.setDurable(true);
        terminal.setTransactional(true);
        return terminal;
    }

    @Bean
    public EventBus eventBus() {
        return new ClusteringEventBus(new DefaultClusterSelector(simpleCluster()), terminal());
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor =
                new AnnotationEventListenerBeanPostProcessor();
        annotationEventListenerBeanPostProcessor.setEventBus(eventBus());
        return annotationEventListenerBeanPostProcessor;
    }

    @Bean
    public SpringResourceInjector springResourceInjector() {
        return new SpringResourceInjector();
    }

}
