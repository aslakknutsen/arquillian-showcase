package com.acme.jpa;

import java.util.List;

public interface Games
{
   public void clear();
   public void add(Game game);
   public List<Game> selectAllUsingJpql();
}
