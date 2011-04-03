package com.acme.cdiejb.mixedinterface;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GreeterDelegate
{
   @Inject
   private GreeterBean greeter;
   
   public String greet(String name)
   {
      return greeter.greet(name);
   }
}
