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

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * A base class for adding a jetty-env.xml descriptor to the WEB-INF directory and
 * a context.xml descriptor to the META-INF directory of an archive.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public abstract class AbstractWeldServletArchiveProcessor {
    public void process(Archive<?> archive) {
        if (archive instanceof WebArchive) {
            WebArchive war = (WebArchive) archive;
            // FIXME: support both Jetty 6 and Jetty 7+
            if (!war.contains("WEB-INF/jetty-env.xml")) {
                war.addAsWebInfResource(getClass().getPackage(), "jetty-env.xml", "jetty-env.xml");
            }
            
            if (!war.contains("META-INF/context.xml")) {
                war.addAsManifestResource(getClass().getPackage(), "context.xml", "context.xml");
            }
        }
    }
}
