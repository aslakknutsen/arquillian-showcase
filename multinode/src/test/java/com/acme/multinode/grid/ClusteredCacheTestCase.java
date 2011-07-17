/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
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
package com.acme.multinode.grid;

import static com.acme.multinode.grid.TestUtils.readInt;
import static com.acme.multinode.grid.Utils.incrementCache;

import java.net.URL;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 3 node Infinispan cluster Demo.
 * 
 * Deploy 3 replicated caches to 3 different Containers and switch between "in container" and "as client" run mode.
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class ClusteredCacheTestCase {
    @Deployment(name = "dep.active-1")
    @TargetsContainer("container.active-1")
    public static WebArchive createTestDeployment() {
        return Deployments.createActiveClient("test-1.war");
    }

    @Deployment(name = "dep.active-2")
    @TargetsContainer("container.active-2")
    public static WebArchive createTestDeployment2() {
        return Deployments.createActiveClient("test-2.war");
    }

    @Deployment(name = "dep.active-3")
    @TargetsContainer("container.active-3")
    public static WebArchive createTestDeployment3() {
        return Deployments.createActiveClient("test-3.war");
    }

    @Inject
    private Cache<String, Integer> cache;

    @Test
    @OperateOnDeployment("dep.active-1")
    public void callActive1() throws Exception {
        int count = incrementCache(cache);
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(1, count);
    }

    @Test
    @OperateOnDeployment("dep.active-2")
    public void callActive2() throws Exception {
        int count = incrementCache(cache);
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(2, count);
    }

    @Test
    @OperateOnDeployment("dep.active-3")
    public void callActive3() throws Exception {
        int count = incrementCache(cache);
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(3, count);
    }

    @Test
    @OperateOnDeployment("dep.active-1")
    public void callActive4() throws Exception {
        int count = incrementCache(cache);
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(4, count);
    }

    @Test
    @OperateOnDeployment("dep.active-3")
    public void callActive5() throws Exception {
        int count = incrementCache(cache);
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(5, count);
    }

    @Test
    @RunAsClient
    @OperateOnDeployment("dep.active-2")
    public void callActive6(@ArquillianResource URL baseURL) throws Exception {
        // @see AS7-1152
        baseURL = new URL("http://localhost:8180/test-1/");
        int count = readInt(baseURL.openStream());
        System.out.println("Cache incremented, current count: " + count);
        Assert.assertEquals(6, count);
    }
}
