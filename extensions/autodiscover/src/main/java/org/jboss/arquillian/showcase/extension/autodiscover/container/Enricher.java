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
package org.jboss.arquillian.showcase.extension.autodiscover.container;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.showcase.extension.autodiscover.ReflectionHelper;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Enricher
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class Enricher implements TestEnricher
{
   private static final String ANNOTATION_CLASS_NAME = "org.mockito.Mock";

   @Override
   public void enrich(Object testCase)
   {
      if(ReflectionHelper.isClassPresent(ANNOTATION_CLASS_NAME))
      {
         enrichFields(testCase);
      }
   }
   
   private void enrichFields(Object testCase)
   {
      List<Field> mockFields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), Mock.class);
      for(Field mockField : mockFields)
      {
         try
         {
            Object mocked = Mockito.mock(mockField.getType());
            if(!mockField.isAccessible())
            {
               mockField.setAccessible(true);
            }
            mockField.set(testCase, mocked);
         }
         catch (Exception e) 
         {
            throw new RuntimeException("Could not inject mocked object on field " + mockField, e);
         }
      }
   }

   @Override
   public Object[] resolve(Method method)
   {
      return new Object[method.getParameterTypes().length];
   }
}
