package org.jboss.arquillian.showcase.extension.deploymentscenario;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReadDeploymentFrom {
    Class<?> value();
    String named() default "_DEFAULT_";
}