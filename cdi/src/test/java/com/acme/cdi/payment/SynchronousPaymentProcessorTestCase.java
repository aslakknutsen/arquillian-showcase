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
package com.acme.cdi.payment;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SynchronousPaymentProcessorTestCase {
    @Deployment
    public static Archive<?> createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);
        // enable the alternative in beans.xml
        beansXml.createAlternatives().clazz(MockPaymentProcessor.class.getName());

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(new StringAsset(beansXml.exportAsString()), beansXml.getDescriptorName())
                .addPackage(Synchronous.class.getPackage());

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(new StringAsset(beansXml.exportAsString()), beansXml.getDescriptorName())
                .addPackage(Synchronous.class.getPackage());

        // return jar => will only work with JBoss AS 7 adapters when using the default JMX protocol and embedded CDI adapters
        // return war => will work in all cases
        // NOTE: Arquillian does not move the custom beans.xml to WEB-INF when
        //       it bundles the jar in a war in order to deploy to a Java EE
        //       container and execute tests over the Servlet protocol.
        return war;
    }

    @Inject
    @Synchronous
    PaymentProcessor syncProcessor;

    @Test
    public void shouldBeReplacedByAMock() throws Exception {
        Double firstPayment = new Double(25);
        Double secondPayment = new Double(50);

        MockPaymentProcessor.PAYMENTS.clear();

        syncProcessor.process(firstPayment);

        assertEquals(1, MockPaymentProcessor.PAYMENTS.size());
        assertEquals(firstPayment, MockPaymentProcessor.PAYMENTS.get(0));

        syncProcessor.process(secondPayment);

        assertEquals(2, MockPaymentProcessor.PAYMENTS.size());
        assertEquals(secondPayment, MockPaymentProcessor.PAYMENTS.get(1));
    }
}
