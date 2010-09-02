package com.acme.ejb.async;

import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(FireAndForget.class)
public class FireAndForgetBean implements FireAndForget
{
   @Override
   @Asynchronous
   public void fire(long busy)
   {
      try
      {
         Thread.sleep(busy);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
   }
   
}
