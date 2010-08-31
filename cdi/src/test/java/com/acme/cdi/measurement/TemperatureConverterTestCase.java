package com.acme.cdi.measurement;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TemperatureConverterTestCase
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(TemperatureConverter.class)
            .addManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Inject
   TemperatureConverter converter;

   @Test
   public void testConvertToCelsius()
   {
      assertEquals(converter.convertToCelsius(32d), 0d, 0d);
      assertEquals(converter.convertToCelsius(212d), 100d, 0d);
   }

   @Test
   public void testConvertToFarenheit()
   {
      assertEquals(converter.convertToFarenheit(0d), 32d, 0d);
      assertEquals(converter.convertToFarenheit(100d), 212d, 0d);
   }

}
