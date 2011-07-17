package com.acme.cdi.decorator;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.cdi.decorate.Announcer;
import com.acme.cdi.decorate.AnnouncerBean;
import com.acme.cdi.decorate.AnnouncerDecorator;

@RunWith(Arquillian.class)
public class AnnouncerDecoratorTestCase {
    @Deployment
    public static Archive<?> createArchive() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Announcer.class.getPackage())
                .addAsManifestResource(
                        new StringAsset(beansXml.decorator(AnnouncerDecorator.class).exportAsString()),
                        beansXml.getDescriptorName());
    }

    @Inject
    AnnouncerBean bean;

    @Test
    public void shouldDecorateAnnouncement() {
        Assert.assertEquals("May I have your attention! School is out!", bean.makeAnnouncement("School is out!"));
    }
}
