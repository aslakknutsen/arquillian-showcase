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
package com.acme.spring.hibernate.repository.impl;

import com.acme.spring.hibernate.domain.Stock;
import com.acme.spring.hibernate.repository.StockRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;

/**
 * <p>A default implementation of {@link StockRepository} that uses Hibernate for persistence operations.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository
public class HibernateStockRepository implements StockRepository {

    /**
     * <p>Represents the instance of {@link SessionFactory} used for persistence operation.</p>
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * <p>Creates new instance of {@link HibernateStockRepository}</p>
     */
    public HibernateStockRepository() {
        // empty constructor
    }

    /**
     * <p>Retrieves current {@link Session}.</p>
     *
     * @return the current session
     */
    protected Session getSession() {

        return sessionFactory.getCurrentSession();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Stock stock) {

        // validates the input
        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        return (Long) getSession().save(stock);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Stock stock) {

        // validates the input
        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        getSession().update(stock);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock get(long id) {

        // retrieves the entity by it's id
        return (Stock) getSession().get(Stock.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock getBySymbol(String symbol) {

        validateNotEmpty(symbol, "symbol");

        // retrieves the entity by it's symbol
        return (Stock) getSession().createQuery("from Stock s where s.symbol = :symbol")
                .setParameter("symbol", symbol)
                .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Stock> getAll() {

        return getSession().createQuery("from Stock").list();
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
