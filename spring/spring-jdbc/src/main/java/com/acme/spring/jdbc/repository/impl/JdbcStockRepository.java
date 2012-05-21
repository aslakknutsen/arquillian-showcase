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
package com.acme.spring.jdbc.repository.impl;

import com.acme.spring.jdbc.domain.Stock;
import com.acme.spring.jdbc.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

/**
 * <p>A default implementation of {@link com.acme.spring.jdbc.repository.StockRepository} that uses JDBC for persistence
 * operations.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository
public class JdbcStockRepository implements StockRepository {

    /**
     * <p>Represents the instance of {@link JdbcTemplate} used for persistence operation.</p>
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * <p>Sets the data source.</p>
     *
     * @param dataSource the data source
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * <p>Creates new instance of {@link JdbcStockRepository}</p>
     */
    public JdbcStockRepository() {
        // empty constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(final Stock stock) {

        // validates the input
        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        // performs the insert in the database
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(
                                "insert into Stock(name, symbol, value, date) values (?, ?, ?, ?)",
                                new String[]{"id"});

                        int index = 1;
                        ps.setString(index++, stock.getName());
                        ps.setString(index++, stock.getSymbol());
                        ps.setBigDecimal(index++, stock.getValue());
                        ps.setDate(index++, new java.sql.Date(stock.getDate().getTime()));
                        return ps;
                    }
                },
                keyHolder);

        // retrieves the value primary key for the inserted key
        stock.setId((Long) keyHolder.getKey());

        return stock.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Stock stock) {

        // validates the input
        validateNoNull(stock, "stock");
        validateNotEmpty(stock.getSymbol(), "symbol");

        // performs the update in database
        jdbcTemplate.update("update Stock set name = ?, symbol = ?, value = ?, date = ? where id = ?",
                stock.getName(), stock.getSymbol(), stock.getValue(), stock.getDate(), stock.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock get(long id) {

        // retrieves the entity by it's id
        return jdbcTemplate.queryForObject("select id, name, symbol, value, date from Stock where id = ?",
                new Object[]{id}, new StockMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stock getBySymbol(String symbol) {

        validateNotEmpty(symbol, "symbol");

        // retrieves the entity by it's symbol
        return jdbcTemplate.queryForObject("select id, name, symbol, value, date from Stock where symbol = ?",
                new Object[]{symbol}, new StockMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Stock> getAll() {

        return jdbcTemplate.query("select id, name, symbol, value, date from Stock", new StockMapper());
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

    /**
     * <p>Stock mapper, used for mapping the rows returned from result set into {@link Stock} objects.</p>
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    private static class StockMapper implements RowMapper<Stock> {

        /**
         * {@inheritDoc}
         */
        public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {

            int index = 1;

            Stock result = new Stock();
            result.setId(rs.getLong(index++));
            result.setName(rs.getString(index++));
            result.setSymbol(rs.getString(index++));
            result.setValue(rs.getBigDecimal(index++));
            result.setDate(rs.getDate(index++));

            return result;
        }
    }
}
