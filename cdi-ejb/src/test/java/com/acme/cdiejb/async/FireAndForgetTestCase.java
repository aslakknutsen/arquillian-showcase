package com.acme.cdiejb.async;

import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FireAndForgetTestCase
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClasses(FireAndForget.class, FireAndForgetBean.class, BlockingFireAndForgetBean.class)
            .addManifestResource(
                  new StringAsset(Descriptors.create(BeansDescriptor.class).alternativeClass(BlockingFireAndForgetBean.class).exportAsString()),
                  "beans.xml");
   }

   @Inject
   FireAndForget asyncBean;

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
