package com.acme.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GameRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Game> fetchAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);
        Root<Game> from = query.from(Game.class);
        CriteriaQuery<Game> select = query.select(from);

        return em.createQuery(select).getResultList();
    }

    public List<Game> fetchAllFor(Platform platform) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);

        Root<Game> fromGames = query.from(Game.class);
        Join<Game, Platform> platformsOfGame = fromGames.join(Game_.supportedPlatforms);
        Predicate givenPlatfromIsAssigned = criteriaBuilder.equal(platformsOfGame.get(Platform_.name), platform.getName());

        CriteriaQuery<Game> allGamesForGivenPlatfrom = query.select(fromGames)
                                                            .where(givenPlatfromIsAssigned);
        return em.createQuery(allGamesForGivenPlatfrom).getResultList();
    }

    public Platform getPlatform(String platformName) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Platform> query = criteriaBuilder.createQuery(Platform.class);

        Root<Platform> fromPlatform = query.from(Platform.class);
        Predicate platofmIsEqualToGivenName = criteriaBuilder.equal(fromPlatform.get(Platform_.name), platformName);
        query.select(fromPlatform).where(platofmIsEqualToGivenName);

        return em.createQuery(query).getSingleResult();
    }

    public List<Game> findOldestGames() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);

        Root<Game> fromGames = query.from(Game.class);
        Predicate oldestYear = criteriaBuilder.equal(fromGames.get(Game_.year), oldestYear());
        query.select(fromGames).where(oldestYear);

        return em.createQuery(query).getResultList();
    }

    private Integer oldestYear() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> query = criteriaBuilder.createQuery(Integer.class);
        Root<Game> fromGames = query.from(Game.class);
        Expression<Integer> oldestYear = criteriaBuilder.min(fromGames.get(Game_.year));
        return em.createQuery(query.select(oldestYear)).getSingleResult();
    }

}