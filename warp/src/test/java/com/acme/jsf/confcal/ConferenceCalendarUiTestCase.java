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
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.extension.phaser.AfterPhase;
import org.jboss.arquillian.warp.extension.phaser.Phase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@WarpTest
public class ConferenceCalendarUiTestCase {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        return ShrinkWrap
                .create(WebArchive.class, "confcal.war")
                .addClasses(Conference.class, ConferenceCalendar.class, CurrentConference.class)
                .addAsWebResource(new File("src/main/webapp/submit.xhtml"))
                .addAsWebResource(new File("src/main/webapp/submission.xhtml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .setWebXML(
                        new StringAsset(webXml.getOrCreateContextParam().paramName(ProjectStage.PROJECT_STAGE_PARAM_NAME)
                                .paramValue(ProjectStage.Development.name()).up().exportAsString()));
    }

    @Test
    @RunAsClient
    public void submittedConferenceShouldBeSaved(Instance<List<Conference>> conferencesInstance) throws Exception {

        Warp.execute(new ClientAction() {
            public void action() {
                browser.navigate().to(contextPath + "submit.jsf");
            }
        }).verify(new VerifyInitialState());

        browser.findElement(By.id("conference:title")).sendKeys("Devoxx");
        browser.findElement(By.id("conference:startDate")).sendKeys("2010-11-15");
        browser.findElement(By.id("conference:endDate")).sendKeys("2010-11-19");
        browser.findElement(By.id("conference:location")).sendKeys("Metropolis, Antwerp, Belgium");
        browser.findElement(By.id("conference:topic")).sendKeys("Java");

        Warp.execute(new ClientAction() {
            public void action() {
                browser.findElement(By.id("conference:submit")).click();
            }
        }).verify(new VerifySubmission());

        assertEquals("Conference Summary", browser.getTitle());
        assertEquals("Devoxx", browser.findElement(By.id("title")).getText());
    }

    public static class VerifyInitialState extends ServerAssertion {

        private static final long serialVersionUID = 1L;

        @Inject
        @CurrentConference
        Conference conference;

        @AfterPhase(Phase.RENDER_RESPONSE)
        public void test_ultimate_answer() {
            assertEquals("/submit.xhtml", FacesContext.getCurrentInstance().getViewRoot().getViewId());

            assertNotNull(conference);
            assertNull(conference.getTitle());
            assertNull(conference.getStartDate());
            assertNull(conference.getEndDate());
            assertNull(conference.getLocation());
            assertNull(conference.getTopic());
        }
    }

    public static class VerifySubmission extends ServerAssertion {

        private static final long serialVersionUID = 1L;

        @Inject
        List<Conference> conferences;

        @AfterPhase(Phase.RENDER_RESPONSE)
        public void verifySubmittedState() {
            assertEquals("/submission.xhtml", FacesContext.getCurrentInstance().getViewRoot().getViewId());

            Conference conference = conferences.get(0);
            assertEquals("Devoxx", conference.getTitle());
            assertEquals(buildDate(2010, 11, 15).toString(), buildDate(conference.getStartDate()).toString());
            assertEquals(buildDate(2010, 11, 19).toString(), buildDate(conference.getEndDate()).toString());
            assertEquals("Metropolis, Antwerp, Belgium", conference.getLocation());
            assertEquals("Java", conference.getTopic());
        }

    }

    private static Date buildDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, 0, 0, 0);
        return cal.getTime();
    }

    private static Date buildDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return buildDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }
}
