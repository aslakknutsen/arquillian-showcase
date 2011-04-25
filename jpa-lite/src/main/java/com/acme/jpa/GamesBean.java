package com.acme.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(Games.class)
public class GamesBean implements Games {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void clear() {
        em.createQuery("delete from Game").executeUpdate();
    }

    @Override
    public void add(Game game) {
        em.persist(game);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Game> selectAllUsingJpql() {
        return em.createQuery("select g from Game g order by g.id").getResultList();
    }
}
