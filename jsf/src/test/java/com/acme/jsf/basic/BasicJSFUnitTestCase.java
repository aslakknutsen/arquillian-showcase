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
package com.acme.jsf.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.faces.application.ProjectStage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BasicJSFUnitTestCase {
    private static final boolean PRINT_RENDERED_OUTPUT = true;

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        return ShrinkWrap.create(WebArchive.class, "basic.war")
                .addClass(HitchhikersGuide.class)
                .addAsWebResource("basic/index.xhtml", "index.xhtml")
                .addAsWebInfResource("common/faces-config.xml", "faces-config.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new StringAsset(webXml.getOrCreateContextParam()
                        .paramName(ProjectStage.PROJECT_STAGE_PARAM_NAME).paramValue(ProjectStage.Development.name()).up()
                        .exportAsString()));
    }

    @Test
    @InitialPage("/index.jsf")
    public void shouldExecutePage(JSFClientSession client, JSFServerSession server) throws Exception {
        validateManagedBeanValueOnIndexPage(client, server);
    }

    @Test
    @InitialPage("/index.jsf")
    public void shouldExecutePageAgain(JSFClientSession client, JSFServerSession server) throws Exception {
        validateManagedBeanValueOnIndexPage(client, server);
    }

    protected void validateManagedBeanValueOnIndexPage(JSFClientSession client, JSFServerSession server) throws Exception {
        if (PRINT_RENDERED_OUTPUT) {
            System.out.println("GET " + server.getFacesContext().getExternalContext().getRequestServletPath() +
                    " HTTP/1.1\n\n" + client.getPageAsText());
        }

        assertTrue(Environment.is12Compatible());
        assertTrue(Environment.is20Compatible());
        assertEquals(2, Environment.getJSFMajorVersion());
        assertEquals(0, Environment.getJSFMinorVersion());

        assertEquals("42", server.getManagedBeanValue("#{hitchhikersGuide.ultimateAnswer}"));
        assertEquals(ProjectStage.Development, server.getManagedBeanValue("#{hitchhikersGuide.journeyStage}"));
        assertEquals(ProjectStage.Development, server.getFacesContext().getApplication().getProjectStage());
    }
}
