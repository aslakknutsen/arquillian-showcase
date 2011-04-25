package com.acme.cdi.singleton;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

@javax.inject.Singleton
public class OnlyOne {
    public static AtomicLong seed = new AtomicLong(0);

    private long id;

    public long getId() {
        return id;
    }

    @PostConstruct
    public void assignId() {
        id = seed.incrementAndGet();
    }
}
