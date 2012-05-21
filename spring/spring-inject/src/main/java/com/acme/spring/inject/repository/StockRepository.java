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
package com.acme.spring.inject.repository;

import com.acme.spring.inject.domain.Stock;

import java.util.List;

/**
 * <p>A stock repository that handles operation in underlying storage.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public interface StockRepository {

    /**
     * <p>Saves a {@link com.acme.spring.inject.domain.Stock} instance in repository.</p>
     *
     * @param stock the {@link com.acme.spring.inject.domain.Stock} instance
     *
     * @return the identifier of newly created stock
     *
     * @throws IllegalArgumentException if stock is null or if stock's symbol is null or empty string
     */
    long save(Stock stock);

    /**
     * <p>Updates the stock in repository.</p>
     *
     * @param stock the stock to update
     *
     * @throws IllegalArgumentException if stock is null or stock's symbol is null or empty string
     */
    void update(Stock stock);

    /**
     * <p>Retrieves the {@link com.acme.spring.inject.domain.Stock} by its id.</p>
     *
     * @param id the id of stock to retrieve
     *
     * @return the stock matching the given id or null if nothing was found
     */
    Stock get(long id);

    /**
     * <p>Retrieves the stock by its symbol.</p>
     *
     * @param symbol the symbol of stock to retrieve
     *
     * @return the found stock matching the given symbol or null if nothing was found
     *
     * @throws IllegalArgumentException if symbol is null or empty string
     */
    Stock getBySymbol(String symbol);

    /**
     * <p>Retrieves the list of all stocks stored in repository.</p>
     *
     * @return the list of all stocks
     */
    List<Stock> getAll();
}
