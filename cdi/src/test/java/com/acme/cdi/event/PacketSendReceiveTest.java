package com.acme.cdi.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Instance;
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
public class PacketSendReceiveTest
{
   @Inject
   Instance<Packet> packetInstance;

   @Inject
   PacketSender sender;

   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Packet.class, PacketSender.class, PacketReceiver.class)
            .addManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void testReceivePacket()
   {
      Packet packet = packetInstance.get();
      assertFalse(packet.isReceived());
      assertNull(packet.getReceiver());
      sender.send(packet);
      assertTrue(packet.isReceived());
      
      // Assertions failing on Weld 1.0.1 SP4 (JBoss AS 6.0.0 M4)
//      assertEquals(packet.getNumberTimesReceived(), 1);
//      assertNull(packet.getReceiver());
   }

   @Test
   public void testReceiveTracerPacket()
   {
      Packet packet = packetInstance.get();
      assertFalse(packet.isReceived());
      assertNull(packet.getReceiver());
      sender.sendTracer(packet);
      assertTrue(packet.isReceived());
      
      // Assertions failing on Weld 1.0.1 SP4 (JBoss AS 6.0.0 M4)
//      assertEquals(packet.getNumberTimesReceived(), 1);
//      assertTrue(packet.getReceiver() instanceof PacketReceiver);
   }
}
