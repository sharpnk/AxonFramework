package org.axonframework.spring.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerAdapter;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventListener;
import org.axonframework.spring.config.annotation.AnnotationDriven;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Allard Buijze
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MessageHandlerSubscriberDefinitionRegistrarTest.Context.class)
public class MessageHandlerSubscriberDefinitionRegistrarTest {

    @Inject
    private EventBus eventBus;
    @Inject
    private EventBus eventBus2;
    @Inject
    private Cluster cluster;
    @Inject
    private EventListener eventListener;
    @Inject
    private CommandBus commandBus;
    @Inject
    private CommandHandler annotationCommandHandler;

    @Test
    public void testHandlersRegisteredToEventBus() throws Exception {
        assertNotNull(eventBus);
        verify(eventBus).subscribe(cluster);
        verify(eventBus2, never()).subscribe(any());
        verify(cluster).subscribe(eventListener);
        verify(commandBus).subscribe(eq(String.class.getName()), eq(annotationCommandHandler));
    }

    @Configuration
    @AnnotationDriven
    @EnableHandlerSubscription(eventBus = "eventBus")
    public static class Context {

        @Bean
        public EventBus eventBus() {
            return mock(EventBus.class);
        }

        @Bean
        public EventBus eventBus2() {
            return mock(EventBus.class);
        }

        @Bean
        public Cluster cluster() {
            return mock(Cluster.class);
        }

        @Bean
        public CommandBus commandBus() {
            return mock(CommandBus.class);
        }

        @Bean
        public EventListener eventListener() {
            return mock(EventListener.class);
        }

        @Bean
        public CommandHandler simpleCommandHandler() {
            return mock(CommandHandler.class);
        }

        @Bean
        public AnnotatedCommandHandler annotationCommandHandler() {
            return new AnnotatedCommandHandler();
        }

    }

    public static class AnnotatedCommandHandler {

        @org.axonframework.commandhandling.annotation.CommandHandler
        public void handle(String command) {

        }
    }
}