/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.ejb;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GreeterTestCase extends Arquillian {
    @Deployment
    public static JavaArchive createDeployment() {
        // explicit archive name required until ARQ-77 is resolved
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(Greeter.class, GreeterBean.class);
    }

    @EJB
    Greeter greeter;

    @Test
    public void shouldBeAbleToInjectEJBAndInvoke() throws Exception {
        String userName = "Earthlings";
        Assert.assertEquals(greeter.greet(userName), "Hello, " + userName);
    }
}
