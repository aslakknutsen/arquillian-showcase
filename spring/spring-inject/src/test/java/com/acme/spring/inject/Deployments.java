/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package com.acme.spring.inject;

import com.acme.spring.inject.domain.Stock;
import com.acme.spring.inject.repository.StockRepository;
import com.acme.spring.inject.repository.impl.DefaultStockRepository;
import com.acme.spring.inject.service.StockService;
import com.acme.spring.inject.service.impl.DefaultStockService;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * <p>A helper class for creating the tests deployments.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class Deployments {

    /**
     * <p>Creates new instance of {@link com.acme.spring.inject.Deployments} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private Deployments() {
        // empty constructor
    }

    /**
     * <p>Creates new tests deployment</p>
     */
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Stock.class, StockRepository.class, StockService.class,
                        DefaultStockRepository.class, DefaultStockService.class)
                .addAsResource("applicationContext.xml");
    }
}
