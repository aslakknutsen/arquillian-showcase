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
package com.acme.spring.inject.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Simple POJO representing a stock.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Stock {

    /**
     * <p>Represents the stock id.</p>
     */
    private long id;

    /**
     * <p>Represents the stock name.</p>
     */
    private String name;

    /**
     * <p>Represents the stock symbol.</p>
     */
    private String symbol;

    /**
     * <p>Represents the stock value.</p>
     */
    private BigDecimal value;

    /**
     * <p>Represents the date.</p>
     */
    private Date date;

    /**
     * <p>Creates new instance of {@link com.acme.spring.inject.domain.Stock}.</p>
     */
    public Stock() {
        // empty constructor
    }

    /**
     * <p>Retrieves the stock id.</p>
     *
     * @return the stock id
     */
    public long getId() {
        return id;
    }

    /**
     * <p>Sets the stock id</p>
     *
     * @param id the stock id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>Retrieves the stock name.</p>
     *
     * @return the stock name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Sets the stock name.</p>
     *
     * @param name the stock name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Retrieves the stock symbol.</p>
     *
     * @return the stock symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * <p>Sets the stock symbol.</p>
     *
     * @param symbol the stock symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * <p>Retrieves the stock value.</p>
     *
     * @return the stock value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * <p>Sets the stock value.</p>
     *
     * @param value the stock value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * <p>Retrieves the stock date.</p>
     *
     * @return the stock date
     */
    public Date getDate() {
        return date;
    }

    /**
     * <p>Retrieves the stock date.</p>
     *
     * @param date the stock date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
