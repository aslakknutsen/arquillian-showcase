/*
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.extension.cdi.test;

import java.util.Properties;

import junit.framework.Assert;

import org.jboss.arquillian.extension.cdi.WeldServletDeploymentAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

/**
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class WeldServletDeploymentAppenderTestCase {

    @Test
    public void testCreateAuxiliaryArchive() throws Exception {
        WeldServletDeploymentAppender appender = new WeldServletDeploymentAppender();
        Archive<?> archive = appender.createAuxiliaryArchive();
        Assert.assertEquals("weld-servlet-1.1.0.Final.jar", archive.getName());
        Assert.assertTrue(archive.contains("META-INF/web-fragment.xml"));
        Assert.assertTrue(archive.contains("META-INF/maven/org.jboss.weld.servlet/weld-servlet/pom.properties"));
        Properties props = new Properties();
        props.load(archive.get("META-INF/maven/org.jboss.weld.servlet/weld-servlet/pom.properties").getAsset().openStream());
        Assert.assertEquals("org.jboss.weld.servlet", props.getProperty("groupId"));
        Assert.assertEquals("weld-servlet", props.get("artifactId"));
    }

}
