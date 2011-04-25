package com.acme.cdiejb.mixedinterface;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebService;

@LocalBean
@Stateless
@WebService
public class GreeterBean implements GreeterRemote {
    public String greet(String name) {
        return "Hello, " + name;
    }
}