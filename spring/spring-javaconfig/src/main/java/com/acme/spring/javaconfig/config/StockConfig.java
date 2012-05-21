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
package com.acme.spring.javaconfig.config;

import com.acme.spring.javaconfig.repository.StockRepository;
import com.acme.spring.javaconfig.repository.impl.DefaultStockRepository;
import com.acme.spring.javaconfig.service.StockService;
import com.acme.spring.javaconfig.service.impl.DefaultStockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Spring configuration class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Configuration
public class StockConfig {

    /**
     * <p>Creates instance of {@link StockService}.</p>
     *
     * @return instance of {@link StockService}
     */
    @Bean
    public StockService createStockService() {

        return new DefaultStockService();
    }

    /**
     * <p>Creates instance of {@link StockRepository}.</p>
     *
     * @return instance of {@link StockRepository}
     */
    @Bean
    public StockRepository createStockRepository() {

        return new DefaultStockRepository();
    }
}
