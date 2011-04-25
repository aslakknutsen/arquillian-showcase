package com.acme.cdi.decorate;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public abstract class AnnouncerDecorator extends AnnouncerBean {
    @Inject
    @Delegate
    private AnnouncerBean delegate;

    @Override
    public String makeAnnouncement(String message) {
        return "May I have your attention! " + delegate.makeAnnouncement(message);
    }
}
