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
package org.jboss.arquillian.extension.cdi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * An {@link AuxiliaryArchiveAppender} implementation that adds the Weld Servlet library to the deployable archive.
 * 
 * <p>
 * The Weld Servlet library is loaded from the JBoss Nexus public repository group. This appender implementation also adds a web
 * fragment descriptor (web-fragment.xml) to the library to enable the Weld listener automatically when the archive is deployed.
 * </p>
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class WeldServletDeploymentAppender implements AuxiliaryArchiveAppender {

    @Override
    public Archive<?> createAuxiliaryArchive() {
        File settingsXml = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            settingsXml = File.createTempFile("arquillian-", "-settings.xml");
            settingsXml.deleteOnExit();
            in = WeldServletDeploymentAppender.class.getResourceAsStream("jboss-repository-settings.xml");
            out = new BufferedOutputStream(new FileOutputStream(settingsXml));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Could not read settings.xml from classpath");
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ioe) {}
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException ioe) {}
            }
        }
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).configureFrom(settingsXml.getAbsolutePath());
        // FIXME get weld-servlet from dependency management, if a pom.xml is available
        JavaArchive weldServlet = resolver.artifact("org.jboss.weld.servlet:weld-servlet:1.1.0.Final")
                .resolveAs(JavaArchive.class).iterator().next();
        WebAppDescriptor webFragmentXml = Descriptors.create(WebAppDescriptor.class);
        return weldServlet.addAsManifestResource(new StringAsset(
                webFragmentXml.listener("org.jboss.weld.environment.servlet.Listener").exportAsString()
                .replaceAll("web-app", "web-fragment").replace("<listener>", "<name>WeldServlet</name><listener>")),
                "web-fragment.xml");
    }
    
}
