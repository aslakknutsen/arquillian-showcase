/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package com.acme.ui;

import static com.acme.ui.Deployments.ARCHIVE_NAME;
import static com.acme.ui.Deployments.BUILD_DIRECTORY;

import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitForHttp;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.id;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.xp;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Arquillian Drone extension against CDI Login example.
 * 
 * Uses Ajocado driver bound to Firefox browser.
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 */
@RunWith(Arquillian.class)
@Ignore
public class LoginScreenAjocadoAcceptanceTestCase {
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, ARCHIVE_NAME)
                .importFrom(new File(BUILD_DIRECTORY + '/' + ARCHIVE_NAME)).as(WebArchive.class);
    }

    private static final String USERNAME = "demo";
    private static final String PASSWORD = "demo";

    private static final XPathLocator LOGGED_IN = xp("//li[contains(text(),'Welcome')]");
    private static final XPathLocator LOGGED_OUT = xp("//li[contains(text(),'Goodbye')]");

    private static final IdLocator USERNAME_FIELD = id("loginForm:username");
    private static final IdLocator PASSWORD_FIELD = id("loginForm:password");

    private static final IdLocator LOGIN_BUTTON = id("loginForm:login");
    private static final IdLocator LOGOUT_BUTTON = id("loginForm:logout");
    
    @Drone
    AjaxSelenium driver;

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testLoginAndLogout() throws Exception {
        Assert.assertNotNull("Deployment URL should not be null", deploymentUrl);

        driver.open(new URL(deploymentUrl + "home.jsf"));
        waitModel.until(elementPresent.locator(USERNAME_FIELD));
        Assert.assertFalse("User should not be logged in!", driver.isElementPresent(LOGOUT_BUTTON));
        driver.type(USERNAME_FIELD, USERNAME);
        driver.type(PASSWORD_FIELD, PASSWORD);

        waitForHttp(driver).click(LOGIN_BUTTON);
        Assert.assertTrue("User should be logged in!", driver.isElementPresent(LOGGED_IN));

        waitForHttp(driver).click(LOGOUT_BUTTON);
        Assert.assertTrue("User should not be logged in!", driver.isElementPresent(LOGGED_OUT));
    }
}
