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
package org.jboss.arquillian.showcase.extension.systemproperties;

import java.util.Properties;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.showcase.extension.systemproperties.client.ArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * ArchiveAppenderTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ArchiveAppenderTestCase extends AbstractTestTestBase
{
   private static final String PREFIX = "ARQ_TEST";
   private static final String PROPERTY_1 = "PROP1";
   private static final String PROPERTY_2 = "PROP2";
   
   private static final String VALUE_1 = "VALUE1";
   private static final String VALUE_2 = "VALUE1";
   
   @Inject
   private Instance<Injector> injector;
   
   private ArquillianDescriptor desc;
   private Archive<?> archive;

   @Before
   public void setup() throws Exception {
      desc = Descriptors.create(ArquillianDescriptor.class);
      archive = ShrinkWrap.create(JavaArchive.class);

      bind(ApplicationScoped.class, ArquillianDescriptor.class, desc);
   }
   
   @Test
   public void shouldNotAddResourceFileIfNoPrefixSet() throws Exception
   {
      ArchiveProcessor processor = new ArchiveProcessor();
      injector.get().inject(processor);
      processor.process(archive, new TestClass(getClass()));
      
      Assert.assertFalse(
            "Verify file was not stored",
            archive.contains(SystemProperties.FILE_NAME));
   }

   @Test
   public void shouldAddResourceFileWhenPrefixSet() throws Exception
   {
      desc.extension(SystemProperties.EXTENSION_NAME)
         .property(SystemProperties.CONFIG_PREFIX, PREFIX);
      
      try 
      {
         System.setProperty(PREFIX + PROPERTY_1, VALUE_1);
         System.setProperty(PREFIX + PROPERTY_2, VALUE_2);

         ArchiveProcessor processor = new ArchiveProcessor();
         injector.get().inject(processor);
         processor.process(archive, new TestClass(getClass()));

         Assert.assertTrue(
               "Verify file was stored",
               archive.contains(SystemProperties.FILE_NAME));
         
         Properties stored = new Properties();
         stored.load(archive.get(SystemProperties.FILE_NAME).getAsset().openStream());
         
         Assert.assertEquals(
               "Verify correct number of properties were filtered", 
               2, stored.size());

         Assert.assertEquals("Verify correct value stored", stored.getProperty(PROPERTY_1), VALUE_1);
         Assert.assertEquals("Verify correct value stored", stored.getProperty(PROPERTY_2), VALUE_2);
      }
      finally
      {
         System.clearProperty(PREFIX + PROPERTY_1);
         System.clearProperty(PREFIX + PROPERTY_2);
      }
   }
}
