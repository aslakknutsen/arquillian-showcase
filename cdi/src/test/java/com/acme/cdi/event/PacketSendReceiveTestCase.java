package com.acme.cdi.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PacketSendReceiveTestCase {
    @Inject
    Instance<Packet> packetInstance;

    @Inject
    PacketSender sender;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Packet.class, PacketSender.class, PacketReceiver.class, Tracer.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testReceivePacket() {
        Packet packet = packetInstance.get();
        assertFalse(packet.isReceived());
        assertNull(packet.getReceiver());
        sender.send(packet);
        assertTrue(packet.isReceived());

        assertEquals(1, packet.getNumberTimesReceived());
        assertNull(packet.getReceiver());
    }

    @Test
    public void testReceiveTracerPacket() {
        Packet packet = packetInstance.get();
        assertFalse(packet.isReceived());
        assertNull(packet.getReceiver());
        sender.sendTracer(packet);
        assertTrue(packet.isReceived());

        assertEquals(1, packet.getNumberTimesReceived());
        assertTrue(packet.getReceiver() instanceof PacketReceiver);
    }
}
