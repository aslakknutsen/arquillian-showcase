package com.acme.jpa;

import java.util.List;

public interface PersistenceService {
    public List<Record> seed(boolean clear);

    public List<Record> selectAll();

    public Record select(Long id, boolean fetchLineItems);

    public boolean isManaged(Record record);

    public void transact();

    public String getProvider();

    public boolean isLazyLoadingPermittedOnClosedSession();
}
