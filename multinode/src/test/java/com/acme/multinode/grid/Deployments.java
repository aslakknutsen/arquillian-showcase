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

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Deployments
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class Deployments {
    public static WebArchive createActiveClient(String name) {
        return ShrinkWrap.create(WebArchive.class, name)
                .addPackage(CacheProducer.class.getPackage())
                .addClass(TestUtils.class)
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml")
                        .artifact("org.infinispan:infinispan-core:4.2.1.FINAL").resolveAsFiles())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsResource("grid/infinispan.xml", "infinispan.xml")
                .setWebXML("grid/in-container-web.xml");
    }
}
