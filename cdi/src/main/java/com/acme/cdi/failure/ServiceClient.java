package com.acme.cdi.failure;

import javax.inject.Inject;

public class ServiceClient {
    @Inject
    private Service service;

    public void invokeServiceOperation() {
        service.operation();
    }
}
