package com.acme.cdi.event;

public class Packet {
    private int receiveCount = 0;
    private Object receiver = null;

    public void ack() {
        receiveCount++;
    }

    public void receivedBy(Object receiver) {
        this.receiver = receiver;
    }

    public boolean isReceived() {
        return receiveCount > 0;
    }

    public int getNumberTimesReceived() {
        return receiveCount;
    }

    public Object getReceiver() {
        return receiver;
    }
}
