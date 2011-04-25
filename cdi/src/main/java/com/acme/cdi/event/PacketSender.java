package com.acme.cdi.event;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

public class PacketSender {
    @Inject
    @Default
    private Event<Packet> defaultPacketEventSrc;

    @Inject
    @Tracer
    private Event<Packet> tracerPacketEventSrc;

    public void send(Packet packet) {
        defaultPacketEventSrc.fire(packet);
    }

    public void sendTracer(Packet packet) {
        tracerPacketEventSrc.fire(packet);
    }
}
