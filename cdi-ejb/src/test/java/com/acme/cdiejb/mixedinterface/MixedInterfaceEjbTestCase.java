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
package com.acme.cdiejb.mixedinterface;

import javax.naming.InitialContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MixedInterfaceEjbTestCase
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      JavaArchive ejbClientJar = ShrinkWrap.create(JavaArchive.class, "client.jar")
         .addManifestResource(EmptyAsset.INSTANCE, "beans.xml")
         .addClass(MixedInterfaceEjbTestCase.class)
         .addClass(GreeterRemote.class);
      
      JavaArchive ejbServiceJar = ShrinkWrap.create(JavaArchive.class, "service.jar")
         .addManifestResource(new StringAsset("<ejb-jar xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
         		"      xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
         		"      xmlns:ejb=\"http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\"\n" +
         		"      xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\"\n" +
         		"      version=\"3.0\">\n" +
         		"   <ejb-client-jar>client.jar</ejb-client-jar>\n" +
         		"</ejb-jar>"), "ejb-jar.xml")
         .addManifestResource(new StringAsset("Manifest-Version: 1.0\nClass-Path: client.jar\n"), "MANIFEST.MF")
         .add(ejbClientJar, "/")
         .addManifestResource(EmptyAsset.INSTANCE, "beans.xml")
         .addClasses(GreeterBean.class, GreeterDelegate.class);
      
      return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
         .addModule(ejbClientJar)
         .addModule(ejbServiceJar);
   }

//   @EJB
//   @Inject
//   GreeterBean greeter;
//   GreeterRemote greeter;
//   @EJB GreeterDelegate greeter;

   @Test
   public void shouldBeAbleToInjectEJBAndInvoke() throws Exception
   {
      String name = "Earthlings";

      GreeterDelegate greeter = (GreeterDelegate) new InitialContext().lookup("java:global/test/service/GreeterDelegate");
      
      Assert.assertEquals("Hello, " + name, greeter.greet(name));
   }
}
