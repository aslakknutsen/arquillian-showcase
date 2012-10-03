/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.showcase.universe.graphene;

import java.net.URL;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.showcase.universe.Deployments;
import org.jboss.arquillian.showcase.universe.Models;
import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.showcase.universe.repository.ConferenceRepository;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.extension.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@WarpTest
@RunWith(Arquillian.class)
public class ConferenceWebClientPageFragmentTestCase {
    
    @Deployment
    public static WebArchive deploy() {
        return Deployments.Client.web()
                .addClass(Deployments.class);
    }
    
	@Drone
	private WebDriver driver;
	
	@ArquillianResource
	private URL baseURL;
	
    @Page
    private ConferencePage page;
    
    @Test @RunAsClient
    public void shouldBeAbleToInsertUser() throws Exception {
        final Conference conference = Models.createRandomConference();
        
        driver.navigate().to(new URL(baseURL, "view/conference/create.jsf"));
        
        final ConferenceForm form = page.getForm()
        		.setId(conference.getId())
        		.setName(conference.getName())
        		.setLocation(conference.getLocation())
        		.setDescription(conference.getDescription())
        		.redirect();
        
        Warp.execute(new ClientAction() {
            @Override
            public void action() {
                form.submit();
            }
        }).verify(new VerifyConference(conference));

    }
    
    public static class VerifyConference extends ServerAssertion {
        
        private static final long serialVersionUID = 1L;

        private Conference conference;
        
        @EJB
        private ConferenceRepository manager;
        
        public VerifyConference(Conference conference) {
            this.conference = conference;
        }
        
        @AfterServlet
        public void wasInserted() {
            Conference stored = manager.get(conference.getId());
            
            Assert.assertEquals(conference.getName(), stored.getName());
            Assert.assertEquals(conference.getLocation(), stored.getLocation());
            Assert.assertEquals(conference.getDescription(), stored.getDescription());
        }
    }
}