package com.acme.cdiejb.nointerface;

import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named
public class NoInterfaceNamedEjb {
    public boolean isFound() {
        return true;
    }
}
