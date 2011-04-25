package com.acme.cdi.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;

public class PacketReceiver {
    public void receive(@Observes @Default Packet packet) {
        packet.ack();
    }

    public void receiverTracer(@Observes @Tracer Packet packet) {
        packet.ack();
        packet.receivedBy(this);
    }
}
