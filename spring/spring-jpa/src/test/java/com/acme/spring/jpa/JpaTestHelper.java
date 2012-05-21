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
package com.acme.spring.jpa;

import com.acme.spring.jpa.domain.Stock;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>A helper class for performing operations on hibernate session.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class JpaTestHelper {

    /**
     * <p>Creates new instance of {@link JpaTestHelper} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private JpaTestHelper() {
        // empty constructor
    }

    /**
     * <p>Executes a sql script.</p>
     *
     * @param entityManager the entity manager
     * @param fileName      the file name
     *
     * @throws java.io.IOException if any error occurs
     */
    public static void runScript(EntityManager entityManager, String fileName) throws IOException {

        // retrieves the resource from class path
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));

        // loads the entire file
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = inputReader.readLine()) != null) {

            if (!line.startsWith("--")) {
                stringBuilder.append(line);
            }
        }

        // splits the commands by semicolon
        String[] commands = stringBuilder.toString().split(";");

        // begins the transaction
        entityManager.getTransaction().begin();

        for (final String command : commands) {

            entityManager.createNativeQuery(command).executeUpdate();
        }

        // commits the transaction
        entityManager.getTransaction().commit();
    }

    /**
     * <p>Retrieves all Stocks stored in database.</p>
     *
     * @param entityManager the entity manager to use
     *
     * @return list of stocks retrieved from database
     */
    public static List<Stock> retrieveAllStocks(EntityManager entityManager) {

        return entityManager.createQuery("from Stock").getResultList();
    }
}
