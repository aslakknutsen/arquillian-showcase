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
package com.acme.jsf.confcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.faces.application.ProjectStage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
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
public class ConferenceCalendarUiTestCase {
    private static final boolean PRINT_RENDERED_OUTPUT = true;

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        return ShrinkWrap.create(WebArchive.class, "confcal.war")
                .addClasses(Conference.class, ConferenceCalendar.class)
                .addAsWebResource("confcal/submit.xhtml", "submit.xhtml")
                .addAsWebResource("confcal/submission.xhtml", "submission.xhtml")
                .addAsWebInfResource("common/faces-config.xml", "faces-config.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new StringAsset(webXml.getOrCreateContextParam()
                        .paramName(ProjectStage.PROJECT_STAGE_PARAM_NAME).paramValue(ProjectStage.Development.name()).up()
                        .exportAsString()));
    }

    @Test
    @InitialPage("/submit.jsf")
    public void submittedConferenceShouldBeSaved(JSFClientSession client, JSFServerSession server, Instance<List<Conference>> conferencesInstance) throws Exception {
        assertEquals("/submit.xhtml", server.getCurrentViewID());

        assertEquals(null, server.getManagedBeanValue("#{conference.title}"));
        assertEquals(null, server.getManagedBeanValue("#{conference.startDate}"));
        assertEquals(null, server.getManagedBeanValue("#{conference.endDate}"));
        assertEquals(null, server.getManagedBeanValue("#{conference.location}"));
        assertEquals(null, server.getManagedBeanValue("#{conference.topic}"));

        if (PRINT_RENDERED_OUTPUT) {
            System.out.println("GET " + server.getFacesContext().getExternalContext().getRequestServletPath() + " HTTP/1.1\n\n"
                    + client.getPageAsText());
        }

        client.setValue("conference:title", "Devoxx");
        client.setValue("conference:startDate", "2010-11-15");
        client.setValue("conference:endDate", "2010-11-19");
        client.setValue("conference:location", "Metropolis, Antwerp, Belgium");
        client.setValue("conference:topic", "Java");
        client.click("conference:submit");

        assertEquals("/submission.xhtml", server.getCurrentViewID());

        // FIXME ELContext is not being properly wrapped when resolving via JSFServerSession
//        @SuppressWarnings("unchecked")
//        List<Conference> conferences = (List<Conference>) server.getManagedBeanValue("#{conferences}");
        List<Conference> conferences = conferencesInstance.get();
        assertNotNull(conferences);
        assertEquals(1, conferences.size());
        Conference conference = conferences.get(0);
        assertEquals("Devoxx", conference.getTitle());
        assertEquals(buildDate(2010, 11, 15).toString(), buildDate(conference.getStartDate()).toString());
        assertEquals(buildDate(2010, 11, 19).toString(), buildDate(conference.getEndDate()).toString());
        assertEquals("Metropolis, Antwerp, Belgium", conference.getLocation());
        assertEquals("Java", conference.getTopic());

        if (PRINT_RENDERED_OUTPUT) {
            System.out.println("POST " + server.getFacesContext().getExternalContext().getRequestServletPath()
                    + " HTTP/1.1\n\n" + client.getPageAsText());
        }

        assertEquals("Devoxx", client.getElement("title").getTextContent().trim());

        // request-scoped beans are gone at this point
    }

    private Date buildDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, 0, 0, 0);
        return cal.getTime();
    }

    private Date buildDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return buildDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }
}
