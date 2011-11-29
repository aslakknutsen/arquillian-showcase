/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package com.acme.jpa;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistenceTestCase {
    private static final String[] GAME_TITLES = { "Super Mario Brothers",
            "Mario Kart", "F-Zero" };

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                         .addPackage(Game.class.getPackage())
                         .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                         .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    // not being injected on GlassFish
    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearDatabase();
        insertData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllGamesInJpql = "select g from Game g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Game> games = em.createQuery(fetchingAllGamesInJpql, Game.class)
                .getResultList();

        // then
        System.out.println("Found " + games.size() + " games (using JPQL)");
        assertContainsAllGames(games);
    }

    @Test
    public void shouldFindAllGamesUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> criteria = builder.createQuery(Game.class);

        Root<Game> game = criteria.from(Game.class);
        criteria.select(game);
        // Toggle comment on first orderBy criteria below (and comment the subsequent line)
        // if you want to try out type-safe criteria queries, a new feature in JPA 2.0
        // requires that the metamodel generator is configured correctly
        // criteria.orderBy(builder.asc(game.get(Game_.id)));
        criteria.orderBy(builder.asc(game.get("id")));

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Game> games = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + games.size() + " games (using Criteria)");
        assertContainsAllGames(games);
    }

    // Private utility methods

    private static void assertContainsAllGames(Collection<Game> retrievedGames) {
        assertEquals(GAME_TITLES.length, retrievedGames.size());
        final Set<String> retrievedGameTitles = new HashSet<String>();
        for (Game game : retrievedGames) {
            retrievedGameTitles.add(game.getTitle());
        }
        assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
    }

    private void clearDatabase() throws Exception {
        utx.begin();
        em.joinTransaction();
        em.createQuery("delete from Game").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        for (String title : GAME_TITLES) {
            Game game = new Game(title);
            em.persist(game);
        }
        utx.commit();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

}
