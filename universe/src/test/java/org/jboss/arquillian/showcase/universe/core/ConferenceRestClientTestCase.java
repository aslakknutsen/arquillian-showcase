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
package org.jboss.arquillian.showcase.universe.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.showcase.universe.Deployments;
import org.jboss.arquillian.showcase.universe.Models;
import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * ConferenceRestClientTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class ConferenceRestClientTestCase {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return Deployments.Client.rest();
    }

    private static Conference conference = Models.createRandomConference();
    
    @ArquillianResource
    private URL baseURL;
    
    @Test @InSequence(1)
    public void shouldBeAbleToPUTConference() throws Exception {
        
        HttpURLConnection connection = (HttpURLConnection)new URL(baseURL, "rest/conference").openConnection();        
        connection.addRequestProperty("content-type", "text/xml");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        toXml(conference, connection.getOutputStream());
        
        Assert.assertEquals(200, connection.getResponseCode());
    }
    
    @Test @InSequence(2)
    public void shouldBeAbleToGETConference() throws Exception {

        HttpURLConnection connection = (HttpURLConnection)new URL(baseURL, "rest/conference/" + conference.getId()).openConnection();        
        connection.addRequestProperty("Accept", "text/xml");
        connection.setRequestMethod("GET");
        
        Conference stored = toObj(connection.getInputStream());
        
        Assert.assertEquals(200, connection.getResponseCode());
       
        Assert.assertEquals(conference.getLocation(), stored.getLocation());
        Assert.assertEquals(conference.getDescription(), stored.getDescription());
        
    }

    private void toXml(Conference conference, OutputStream output) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Conference.class);
        context.createMarshaller().marshal(conference, output);
    }

    private Conference toObj(InputStream input) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Conference.class);
        return (Conference)context.createUnmarshaller().unmarshal(input);
    }
}
