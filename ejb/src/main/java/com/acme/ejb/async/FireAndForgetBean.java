package com.acme.ejb.async;

import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(FireAndForget.class)
public class FireAndForgetBean extends AbstractFireAndForgetBean
{
   @Override
   @Asynchronous
   public void fire(long busy)
   {
      super.fire(busy);
   }
   
}
