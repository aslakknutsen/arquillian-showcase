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

import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * A {@link ProtocolArchiveProcessor} implementation that adds a jetty-env.xml descriptor to the WEB-INF directory and
 * a context.xml descriptor to the META-INF directory of the test archive for in-container tests.
 * 
 * <p>
 * These descriptors effectively bind the BeanManager to the JNDI name java:/comp/env/BeanManager when the test archive gets
 * deployed to Jetty or Tomcat.
 * </p>
 * 
 * <p>
 * The descriptors are not added if there is a descriptor already present in the {@link WebArchive} at the target path.
 * </p>
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class WeldServletProtocolArchiveProcessor extends AbstractWeldServletArchiveProcessor implements ProtocolArchiveProcessor {

    @Override
    public void process(TestDeployment testDeployment, Archive<?> protocolArchive) {
        super.process(protocolArchive);
    }

}
