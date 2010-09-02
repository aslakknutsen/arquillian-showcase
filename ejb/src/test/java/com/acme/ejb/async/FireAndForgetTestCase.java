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
package com.acme.ejb.async;

import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
//@Ignore
public class FireAndForgetTestCase
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClasses(FireAndForget.class, AbstractFireAndForgetBean.class,
                  BlockingFireAndForget.class, BlockingFireAndForgetBean.class);
   }

   @EJB
   BlockingFireAndForget asyncBean;

   @Test
   public void shouldInvokeAsynchronously() throws Exception
   {
      // use latch barrier or something to verify code is executing asynchronously
      System.out.println("Current thread [id=" + Thread.currentThread().getId() + "; name=" + Thread.currentThread().getName() + "]");
      asyncBean.fire(1000);
      System.out.println("Async operation fired");
      BlockingFireAndForgetBean.LATCH.await(30, TimeUnit.SECONDS);
      System.out.println("Async thread complete");
      assertNull(BlockingFireAndForgetBean.threadValue.get());
   }
}
