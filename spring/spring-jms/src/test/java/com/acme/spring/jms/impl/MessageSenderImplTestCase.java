/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.spring.jms.impl;

import com.acme.spring.jms.Deployments;
import com.acme.spring.jms.MessageSender;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.test.annotation.SpringConfiguration;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests the {@link MessageSenderImpl} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
@SpringConfiguration("applicationContext.xml")
public class MessageSenderImplTestCase {

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static Archive createTestArchive() {

        return Deployments.createDeployment();
    }

    /**
     * <p>Injected {@link MessageSenderImpl}.</p>
     */
    @Autowired
    private MessageSender messageSender;

    /**
     * <p>Injected {@link JmsTemplate}</p>
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * <p>Tests the {@link MessageSenderImpl#sendMessage(String)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testSend() throws Exception {

        String message = "Hello world";

        messageSender.sendMessage(message);

        Message result = jmsTemplate.receive("SpringTestQueue");

        assertNotNull("The received message was null.", result);
        assertTrue("The received message had invalid type.", result instanceof TextMessage);
        assertEquals("The received message had invalid text.", message, ((TextMessage) result).getText());
        
        System.out.println(((TextMessage) result).getText());
    }
}
