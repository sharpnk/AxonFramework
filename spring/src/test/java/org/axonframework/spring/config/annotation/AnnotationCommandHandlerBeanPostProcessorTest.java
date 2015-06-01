/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.spring.config.annotation;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Allard Buijze
 */
public class AnnotationCommandHandlerBeanPostProcessorTest {

    private AnnotationCommandHandlerBeanPostProcessor testSubject;

    @Before
    public void setUp() {
        testSubject = new AnnotationCommandHandlerBeanPostProcessor();
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void testEventHandlerCallsRedirectToAdapter() throws Throwable {
        Object result1 = testSubject.postProcessBeforeInitialization(new AnnotatedCommandHandler(), "beanName");
        Object postProcessedBean = testSubject.postProcessAfterInitialization(result1, "beanName");

        assertTrue(postProcessedBean instanceof CommandHandler);
        assertTrue(postProcessedBean instanceof AnnotatedCommandHandler);

        CommandHandler<MyCommand> commandHandler = (CommandHandler<MyCommand>) postProcessedBean;
        AnnotatedCommandHandler annotatedCommandHandler = (AnnotatedCommandHandler) postProcessedBean;
        CommandMessage<MyCommand> myCommand = GenericCommandMessage.asCommandMessage(new MyCommand());
        commandHandler.handle(myCommand, null);

        assertEquals(1, annotatedCommandHandler.getInvocationCount());
    }

    public static class AnnotatedCommandHandler {

        private int invocationCount;

        @SuppressWarnings({"UnusedDeclaration"})
        @org.axonframework.commandhandling.annotation.CommandHandler
        public void handleCommand(MyCommand command) {
            invocationCount++;
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }

    private static class MyCommand {

    }
}