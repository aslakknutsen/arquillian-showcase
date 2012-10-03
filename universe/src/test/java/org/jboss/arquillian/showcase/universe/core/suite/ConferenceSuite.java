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
package org.jboss.arquillian.showcase.universe.core.suite;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.showcase.universe.Deployments;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * ConferenceSuite
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
//@RunWith(ArquillianSuite.class)
//@Sub({ConferenceMemberContainer.class, ConferenceMemberClient.class})
public class ConferenceSuite
{
   @Deployment
   public static WebArchive deploy() {
       return Deployments.Client.rest()
             .addClasses(ConferenceMemberContainer.class, ConferenceMemberClient.class);
   }

}
