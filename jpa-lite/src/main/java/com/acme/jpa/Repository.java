package com.acme.jpa;

import java.io.Serializable;
import java.util.List;

public interface Repository
{
   public void create(Serializable entity);
   public void delete(Serializable entity);
   public <T> T retrieveById(Class<T> type, Long id);
   public <T> List<T> retrieveByQuery(Class<T> type, String query, String... params);
   public void update(Serializable entity);
   public void saveNonManaged(Serializable entity);
   public boolean isManaging(Serializable entity);
   public void close();
}