package com.acme.cdi.decorate;

public class AnnouncerBean implements Announcer // If this interface is removed, test fails. Can only decorate interface?
{
    public String makeAnnouncement(String message) {
        return message;
    }
}
