package com.acme.jpa;

import java.util.List;

public interface PersistenceManager
{
   public List<Record> selectAll();
   public Record select(Long id, boolean fetchLineItems);
   public boolean isManaged(Record record);
}
