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
package com.acme.spring.inject.repository.impl;

import com.acme.spring.inject.domain.Stock;
import com.acme.spring.inject.repository.StockRepository;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A default implementation of {@link com.acme.spring.inject.repository.StockRepository}.</p>
 *
 * <p>This is a pretty simple implementation that stores all the stocks in a memory.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository
public class DefaultStockRepository implements StockRepository {

    /**
     * <p>Represents map of stocks.</p>
     */
    private final Map<String, Stock> stockMap = new HashMap<String, Stock>();

    /**
     * <p>Creates new instance of {@link com.acme.spring.inject.repository.impl.DefaultStockRepository}</p>
     */
    public DefaultStockRepository() {
        // empty constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Stock stock) {

        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        // generates 'unique' identifier
        stock.setId((long) (Math.random() * 10E4D));

        // saves the stock in map
        stockMap.put(stock.getSymbol(), stock);

        // returns the assigned id
        return stock.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Stock stock) {

        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        for (Map.Entry<String, Stock> entry : stockMap.entrySet()) {

            if (entry.getValue() == stock) {

                stockMap.remove(entry.getKey());
            }
        }

        stockMap.put(stock.getSymbol(), stock);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock get(long id) {

        for (Stock stock : stockMap.values()) {

            if (stock.getId() == id) {
                return stock;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock getBySymbol(String symbol) {

        validateNotEmpty(symbol, "symbol");

        return stockMap.get(symbol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Stock> getAll() {

        return new ArrayList<Stock>(stockMap.values());
    }

    /**
     * <p>Validates that the passed parameter is not null or empty string, in case it isn't than {@link
     * IllegalArgumentException} is being thrown.</p>
     *
     * @param param         the parameter to validate
     * @param parameterName the parameter name
     */
    private void validateNotEmpty(String param, String parameterName) {
        validateNoNull(param, parameterName);

        if (param.trim().length() == 0) {
            throw new IllegalArgumentException(MessageFormat.format("The {0} can not be null or empty string.",
                    parameterName));
        }
    }

    /**
     * <p>Validates that the passed parameter is not null, in case it's null than the {@link IllegalArgumentException}
     * is being thrown.</p>
     *
     * @param param         the parameter to validate
     * @param parameterName the parameter name
     */
    private void validateNoNull(Object param, String parameterName) {
        if (param == null) {
            throw new IllegalArgumentException(MessageFormat.format("The '{0}' can not be null.", parameterName));
        }
    }
}
