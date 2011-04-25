package com.acme.cdiejb.mixedinterface;

import javax.ejb.Remote;

@Remote
public interface GreeterRemote {
    public String greet(String name);
}
