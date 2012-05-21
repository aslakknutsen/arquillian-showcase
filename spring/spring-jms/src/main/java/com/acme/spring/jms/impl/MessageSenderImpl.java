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

import com.acme.spring.jms.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * <p>Implementation of {@link MessageSender} that sends messages through JMS.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Component
public class MessageSenderImpl implements MessageSender {

    /**
     * <p>Injected {@link JmsTemplate} that is used for sending the messages.</p>
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * {@inheritDoc}
     */
    public void sendMessage(final String message) {

        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {

                return session.createTextMessage(message);
            }
        });
    }
}
