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

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SynchronousPaymentProcessorTestCase {
    @Deployment
    public static JavaArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(
                        new StringAsset(beansXml.alternativeClass(MockPaymentProcessor.class).exportAsString()),
                        beansXml.getDescriptorName())
                // .addAsManifestResource(SynchronousPaymentProcessorTestCase.class.getPackage(), "beans.xml", "beans.xml")
                .addPackage(Synchronous.class.getPackage());
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

    // Use this deployment for GlassFish due to visibility issues
    /*
     * @Deployment public static WebArchive createDeployment() { BeansDescriptor beansXml =
     * Descriptors.create(BeansDescriptor.class);
     * 
     * return ShrinkWrap.create(WebArchive.class) .addAsWebInfResource(new
     * StringAsset(beansXml.alternativeClass(MockPaymentProcessor.class).exportAsString()), beansXml.getDescriptorName())
     * .addPackage(Synchronous.class.getPackage()); }
     */
}
