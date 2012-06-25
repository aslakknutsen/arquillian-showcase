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
package org.jboss.arquillian.showcase.universe;

import java.io.File;

import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.showcase.universe.model.User;
import org.jboss.arquillian.showcase.universe.repository.ConferenceRepository;
import org.jboss.arquillian.showcase.universe.rest.ConferenceRestEndpoint;
import org.jboss.arquillian.showcase.universe.view.ConferenceBean;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class Deployments {

    public static class Backend {
    
        public static JavaArchive conference() {
            return ShrinkWrap.create(JavaArchive.class, "conference-backend.jar")
                    // Test helpers
                    .addClasses(Models.class) 
                    // Models
                    .addClasses(Conference.class, User.class, ConferenceRepository.class)
                    // Descriptors
                    .addAsManifestResource(new File("src/test/resources/test-persistence.xml"), "persistence.xml")
                    .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml");
        }
    }
    
    public static class Client {
        
        public static WebArchive web() {
            return base("conference-web.war")
                    .addClasses(ConferenceBean.class)
                    
                    // Web Resources
                    .addAsWebResource(new File("src/main/webapp/view/conference/create.xhtml"), "view/conference/create.xhtml")
                    .addAsWebResource(new File("src/main/webapp/view/page.xhtml"), "view/page.xhtml")
                    
                    // Descriptors
                    .setWebXML(new File("src/test/resources/test-web.xml"))
                    .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
                    .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml");
        }

        public static WebArchive rest() {
            return base("conference-rest.war")
                    .addClasses(ConferenceRestEndpoint.class)
                    
                    // Descriptors
                    .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                    .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml");
        }
        
        private static WebArchive base(String name) {
            return ShrinkWrap.create(WebArchive.class, name)
                    
                    // Backend library
                    .addAsLibraries(Backend.conference());
        }
    }
}
