package com.acme.cdi.event;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BeanManagerInitializedTestCase {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(ApplicationInitializer.class, BeanManagerInitializedExtension.class, Initialized.class)
                .addAsServiceProvider(Extension.class, BeanManagerInitializedExtension.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void should_observe_startup_event() {
        assertTrue(ApplicationInitializer.RECEIVED_STARTUP_EVENT);
    }
}
