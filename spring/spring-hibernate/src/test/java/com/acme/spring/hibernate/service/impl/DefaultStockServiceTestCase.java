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
package com.acme.spring.hibernate.service.impl;

import com.acme.spring.hibernate.Deployments;
import com.acme.spring.hibernate.domain.Stock;
import com.acme.spring.hibernate.service.StockService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.test.annotation.SpringConfiguration;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.acme.spring.hibernate.HibernateTestHelper.retrieveAllStocks;
import static com.acme.spring.hibernate.HibernateTestHelper.runScript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
@SpringConfiguration("applicationContext.xml")
public class DefaultStockServiceTestCase {

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static Archive createTestArchive() {

        return Deployments.createDeployment();
    }

    /**
     * <p>Injected {@link com.acme.spring.hibernate.service.impl.DefaultStockService}.</p>
     */
    @Autowired
    private StockService stockService;

    /**
     * <p>{@link org.hibernate.SessionFactory} instance used by tests.</p>
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * <p>Retrieves current {@link Session}.</p>
     *
     * @return the current session
     */
    public Session getSession() {

        return sessionFactory.getCurrentSession();
    }

    /**
     * <p>Tears down the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @After
    public void tearDown() throws Exception {

        // deletes all records from database
        getSession().getTransaction().begin();
        getSession().createQuery("delete from Stock").executeUpdate();
        getSession().getTransaction().commit();
    }

    /**
     * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService#save(Stock)} method.</p>
     */
    @Test
    public void testSave() {

        Stock acme = createStock("Acme", "ACM", 123.21D, new Date());
        Stock redhat = createStock("Red Hat", "RHC", 59.61D, new Date());

        getSession().getTransaction().begin();

        stockService.save(acme);
        stockService.save(redhat);

        assertTrue("The stock id hasn't been assigned.", acme.getId() > 0);
        assertTrue("The stock id hasn't been assigned.", redhat.getId() > 0);

        List<Stock> stocks = retrieveAllStocks(getSession());

        getSession().getTransaction().commit();

        assertEquals("Incorrect number of created stocks, 2 were expected.", 2, stocks.size());

        assertStock(acme, stocks.get(0));
        assertStock(redhat, stocks.get(1));
    }

    /**
     * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService#update(Stock)} method.</p>
     */
    @Test
    public void testUpdate() throws Exception {

        getSession().getTransaction().begin();

        runScript(getSession(), "insert.sql");

        List<Stock> stocks = retrieveAllStocks(getSession());

        Stock acme = stocks.get(0);
        acme.setSymbol("ACE");

        stockService.update(acme);

        stocks = retrieveAllStocks(getSession());

        getSession().getTransaction().commit();

        assertEquals("The stock symbol hasn't been updated.", acme.getSymbol(), stocks.get(0).getSymbol());
    }

    /**
     * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService#get(long)} method.</p>
     */
    @Test
    public void testGet() throws Exception {

        getSession().getTransaction().begin();
        runScript(getSession(), "insert.sql");

        Stock acme = createStock("Acme", "ACM", 123.21D, new Date());

        Stock result = stockService.get(1L);

        getSession().getTransaction().commit();

        assertNotNull("Method returned null result.", result);
        assertStock(acme, result);
    }

    /**
     * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService#getBySymbol(String)} method.</p>
     */
    @Test
    public void testGetBySymbol() throws Exception {

        getSession().getTransaction().begin();
        runScript(getSession(), "insert.sql");

        Stock acme = createStock("Acme", "ACM", 123.21D, new Date());

        Stock result = stockService.getBySymbol(acme.getSymbol());

        getSession().getTransaction().commit();

        assertNotNull("Method returned null result.", result);
        assertStock(acme, result);
    }

    /**
     * <p>Tests the {@link com.acme.spring.hibernate.service.impl.DefaultStockService#getAll()} method.</p>
     */
    @Test
    public void testGetAll() throws Exception {

        getSession().getTransaction().begin();
        runScript(getSession(), "insert.sql");

        List<Stock> result = stockService.getAll();

        getSession().getTransaction().commit();

        assertNotNull("Method returned null result.", result);
        assertEquals("Incorrect number of elements.", 2, result.size());
    }

    /**
     * <p>Creates new stock instance</p>
     *
     * @param name   the stock name
     * @param symbol the stock symbol
     * @param value  the stock value
     * @param date   the stock date
     *
     * @return the created stock instance
     */
    private static Stock createStock(String name, String symbol, double value, Date date) {

        Stock result = new Stock();
        result.setName(name);
        result.setSymbol(symbol);
        result.setValue(new BigDecimal(value));
        result.setDate(date);
        return result;
    }

    /**
     * <p>Asserts that the actual stock's properties values are correct.</p>
     *
     * @param expected the expected stock object
     * @param actual   the tested stock object
     */
    private static void assertStock(Stock expected, Stock actual) {

        assertEquals("Stock has invalid id property.", expected.getId(), expected.getId());
        assertEquals("Stock has invalid name property.", expected.getName(), expected.getName());
        assertEquals("Stock has invalid symbol property.", expected.getSymbol(), expected.getSymbol());
        assertEquals("Stock has invalid value property.", expected.getValue(), expected.getValue());
    }
}
