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

import java.io.File;
import java.net.URL;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@WarpTest
public class BasicJSFUnitTestCase {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        return ShrinkWrap
                .create(WebArchive.class, "basic.war")
                .addClass(HitchhikersGuide.class)
                .addAsWebResource(new File("src/main/webapp/index.xhtml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .setWebXML(
                        new StringAsset(webXml.getOrCreateContextParam().paramName(ProjectStage.PROJECT_STAGE_PARAM_NAME)
                                .paramValue(ProjectStage.Development.name()).up().exportAsString()));
    }

    @Test
    @RunAsClient
    public void shouldExecutePage() throws Exception {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.navigate().to(contextPath + "index.jsf");
            }
        }).inspect(new verifyBeanValueOnInitialPage());
    }

    public static class verifyBeanValueOnInitialPage extends Inspection {

        private static final long serialVersionUID = 1L;

        @Inject
        HitchhikersGuide hitchhikersGuide;

        @AfterPhase(Phase.RENDER_RESPONSE)
        public void test_ultimate_answer() {
            assertEquals("42", hitchhikersGuide.getUltimateAnswer());
            assertEquals(ProjectStage.Development, FacesContext.getCurrentInstance().getApplication().getProjectStage());
        }

    }
}
