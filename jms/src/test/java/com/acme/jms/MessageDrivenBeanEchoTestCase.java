/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package com.acme.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MessageDrivenBeanEchoTestCase {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(MessageEchoBean.class);
    }

    private static final long QUALITY_OF_SERVICE_THRESHOLD_MS = 5 * 60 * 1000;

    @Resource(mappedName = "/queue/DLQ")
    Queue dlq;

    @Resource(mappedName = "/ConnectionFactory")
    ConnectionFactory factory;

    @Test
    public void shouldBeAbleToSendMessage() throws Exception {
        String messageBody = "ping";

        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(dlq);
            MessageConsumer consumer = session.createConsumer(tempQueue);

            connection.start();

            Message request = session.createTextMessage(messageBody);
            request.setJMSReplyTo(tempQueue);

            producer.send(request);
            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
            assertNotNull(response);
            String responseBody = ((TextMessage) response).getText();

            assertEquals("Should have responded with same message", messageBody, responseBody);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
