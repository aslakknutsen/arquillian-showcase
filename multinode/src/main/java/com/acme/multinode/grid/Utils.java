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

import org.infinispan.Cache;

/**
 * Utils
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public final class Utils {
    private Utils() {
    }

    public static Integer incrementCache(Cache<String, Integer> cache) {
        String key = "counter";
        Integer counter = cache.get(key);
        Integer newCounter;
        if (counter != null) {
            newCounter = counter.intValue() + 1;
        } else {
            newCounter = 1;
        }
        cache.put(key, newCounter);
        return newCounter;
    }
}
