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
package org.jboss.arquillian.showcase.extension.deploymentscenario;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentScenarioGenerator;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;

/**
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ExternalScenarioGenerator implements DeploymentScenarioGenerator
{
   public static final String EXTENSION_NAME = "deployments";
   
   @Inject
   private Instance<ArquillianDescriptor> descriptor;
   
   @Override
   public List<DeploymentDescription> generate(TestClass testClass)
   {
      List<DeploymentDescription> descriptions = new ArrayList<DeploymentDescription>();
      
      Map<String, String> mappings = getConfiguration();
      
      Method deploymentMethod = null;
      
      try {
         deploymentMethod = getDeploymentMethod(mappings, testClass);
      }
      catch (Exception e) {
         throw new RuntimeException("Could not find externalized @Deployment for TestClass: " + testClass.getName(), e);
      } 
      
      try {
         Archive<?> archive = (Archive<?>)deploymentMethod.invoke(null);
         descriptions.add(new DeploymentDescription("_NO_NAME_", archive));
      }
      catch (Exception e) {
         throw new RuntimeException("Could not read externalized @Deployment from method: " + deploymentMethod, e);
      }
      
      return descriptions;
   }

   private Method getDeploymentMethod(Map<String, String> mappings, TestClass testClass) throws Exception
   {
      String mapping = mappings.get(testClass.getJavaClass().getSimpleName()).trim();

      String className = mapping.substring(0, mapping.indexOf('#'));
      String methodName = mapping.substring(mapping.indexOf('#')+1, mapping.length());
      
      return Class.forName(className).getMethod(methodName);
   }

   private Map<String, String> getConfiguration()
   {
      for(ExtensionDef def : descriptor.get().getExtensions())
      {
         if(EXTENSION_NAME.equalsIgnoreCase(def.getExtensionName()))
         {
            return def.getExtensionProperties();
         }
      }
      return new HashMap<String, String>();
   }
}
