package com.acme.cdi.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

public class BeanManagerInitializedExtension implements Extension {
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        manager.fireEvent(manager, new AnnotationLiteral<Initialized>() {
        });
    }
}
