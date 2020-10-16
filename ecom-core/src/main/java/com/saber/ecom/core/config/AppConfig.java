package com.saber.ecom.core.config;

import com.mongodb.Mongo;
import com.saber.ecom.core.inventory.model.Inventory;
import com.saber.ecom.core.order.model.Order;
import com.saber.ecom.core.order.saga.OrderProcessSaga;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.*;
import org.axonframework.eventhandling.amqp.DefaultAMQPMessageConverter;
import org.axonframework.eventhandling.amqp.spring.ListenerContainerLifecycleManager;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPTerminal;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.repository.GenericJpaRepository;
import org.axonframework.saga.GenericSagaFactory;
import org.axonframework.saga.SagaManager;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.annotation.AnnotatedSagaManager;
import org.axonframework.saga.repository.mongo.DefaultMongoTemplate;
import org.axonframework.saga.repository.mongo.MongoSagaRepository;
import org.axonframework.saga.repository.mongo.MongoTemplate;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.axonframework.unitofwork.SpringTransactionManager;
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
import org.springframework.transaction.PlatformTransactionManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

@Configuration
@RefreshScope
public class AppConfig {
    @PersistenceContext
    private EntityManager entityManager;
    private final PlatformTransactionManager transactionManager;

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

    private final Mongo mongo;

    public AppConfig(PlatformTransactionManager transactionManager, Mongo mongo) {
        this.transactionManager = transactionManager;
        this.mongo = mongo;
    }


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
    public SimpleCommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.setDispatchInterceptors(Collections.singletonList(new BeanValidationInterceptor()));
        SpringTransactionManager transactionManager =
                new SpringTransactionManager(this.transactionManager);
        commandBus.setTransactionManager(transactionManager);
        return commandBus;
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
        ClusteringEventBus clusteringEventBus = new ClusteringEventBus(new DefaultClusterSelector(simpleCluster()), terminal());
        clusteringEventBus.subscribe(sagaManager());
        return clusteringEventBus;
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor =
                new AnnotationEventListenerBeanPostProcessor();
        annotationEventListenerBeanPostProcessor.setEventBus(eventBus());
        return annotationEventListenerBeanPostProcessor;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor =
                new AnnotationCommandHandlerBeanPostProcessor();
        annotationCommandHandlerBeanPostProcessor.setCommandBus(commandBus());
        return annotationCommandHandlerBeanPostProcessor;
    }

    @Bean(name = "commandGateway")
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<CommandGateway> factoryBean = new CommandGatewayFactoryBean<>();
        factoryBean.setCommandBus(commandBus());
        return factoryBean;
    }

    @Bean
    public SimpleEntityManagerProvider simpleEntityManagerProvider() {
        return new SimpleEntityManagerProvider(entityManager);
    }

    @Bean(name = "inventoryRepository")
    public GenericJpaRepository<Inventory> inventoryGenericJpaRepository() {
        SimpleEntityManagerProvider entityManagerProvider = simpleEntityManagerProvider();
        GenericJpaRepository<Inventory> inventoryGenericJpaRepository =
                new GenericJpaRepository<>(entityManagerProvider, Inventory.class);
        inventoryGenericJpaRepository.setEventBus(eventBus());
        return inventoryGenericJpaRepository;
    }

    @Bean(name = "orderRepository")
    public GenericJpaRepository<Order> orderGenericJpaRepository() {
        SimpleEntityManagerProvider entityManagerProvider = simpleEntityManagerProvider();
        GenericJpaRepository<Order> orderGenericJpaRepository =
                new GenericJpaRepository<>(entityManagerProvider, Order.class);
        orderGenericJpaRepository.setEventBus(eventBus());
        return orderGenericJpaRepository;
    }

    @Bean
    public SpringResourceInjector springResourceInjector() {
        return new SpringResourceInjector();
    }

    @Bean(name = "mongoTemplateAxon")
    public MongoTemplate defaultMongoTemplate() {
        return new DefaultMongoTemplate(mongo);
    }

    @Bean(name = "sagaRepository")
    public SagaRepository sagaRepository() {
        MongoSagaRepository sagaRepository = new MongoSagaRepository(defaultMongoTemplate());
        sagaRepository.setResourceInjector(springResourceInjector());
        return sagaRepository;
    }

    @Bean(name = "sagaManager")
    @SuppressWarnings("unchecked")
    public SagaManager sagaManager() {
        GenericSagaFactory sagaFactory = new GenericSagaFactory();
        sagaFactory.setResourceInjector(springResourceInjector());
        AnnotatedSagaManager sagaManager =
                new AnnotatedSagaManager(sagaRepository(), sagaFactory, OrderProcessSaga.class);
        sagaManager.setSynchronizeSagaAccess(false);
        sagaManager.setSuppressExceptions(false);
        return sagaManager;
    }
}
