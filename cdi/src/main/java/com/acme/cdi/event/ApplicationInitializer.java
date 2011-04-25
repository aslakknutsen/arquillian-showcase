package com.acme.cdi.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;

public class ApplicationInitializer {
    public static boolean RECEIVED_STARTUP_EVENT = false;

    public void onStartup(@Observes @Initialized BeanManager beanManager) {
        RECEIVED_STARTUP_EVENT = true;
    }
}
