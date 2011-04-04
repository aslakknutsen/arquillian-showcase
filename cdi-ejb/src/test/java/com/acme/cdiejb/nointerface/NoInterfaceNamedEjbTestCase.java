package com.acme.cdiejb.nointerface;

import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class NoInterfaceNamedEjbTestCase
{
   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClasses(NoInterfaceNamedEjb.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void should_inject_invokable_ejb_reference(NoInterfaceNamedEjb bean) throws Exception
   {
      Assert.assertNotNull(bean);
      Assert.assertTrue(bean.isFound());
   }
   
   @Test
   public void should_locate_named_ejb(BeanManager bm) throws Exception
   {
      Iterator<Bean<?>> results = bm.getBeans("noInterfaceNamedEjb").iterator();
      Assert.assertTrue(results.hasNext());
      Bean<?> bean = results.next();
      Assert.assertFalse(results.hasNext());
      Assert.assertTrue(bean.getBeanClass().equals(NoInterfaceNamedEjb.class));
      @SuppressWarnings("unchecked")
      Bean<NoInterfaceNamedEjb> typedBean = (Bean<NoInterfaceNamedEjb>) bean;
      CreationalContext<NoInterfaceNamedEjb> cc = null;
      try
      {
         cc = bm.createCreationalContext(typedBean);
         NoInterfaceNamedEjb beanRef = typedBean.create(cc);
         Assert.assertTrue(beanRef.isFound());
      }
      finally
      {
         if (cc != null)
         {
            cc.release();
         }
      }
   }
}
