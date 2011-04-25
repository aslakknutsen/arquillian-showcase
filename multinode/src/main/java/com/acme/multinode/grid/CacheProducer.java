/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package com.acme.multinode.grid;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author Galder Zamarreño
 */
public class CacheProducer {
    // @Resource(name = "name")
    private String configName = "infinispan.xml";

    @Produces
    @ApplicationScoped
    public EmbeddedCacheManager createCacheManager() throws IOException {
        return new DefaultCacheManager(configName);
    }

    public void distroyCacheManager(@Disposes EmbeddedCacheManager manager) {
        manager.stop();
    }

    @Produces
    @ApplicationScoped
    public Cache<String, Integer> createCache(EmbeddedCacheManager cacheManager) {
        return cacheManager.getCache();
    }

    public void distroyCache(@Disposes Cache<String, Integer> cache) {
        cache.stop();
    }
}
