package com.acme.ejb.async;

public class AbstractFireAndForgetBean implements FireAndForget
{
   @Override
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
