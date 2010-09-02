package com.acme.ejb.async;

import java.util.concurrent.CountDownLatch;

import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

@Alternative
@Stateless
@Local(FireAndForget.class)
public class BlockingFireAndForgetBean extends FireAndForgetBean
{
   public static CountDownLatch LATCH = new CountDownLatch(1);

   public static ThreadLocal<Long> threadValue = new ThreadLocal<Long>();

   @Override
   @Asynchronous
   public void fire(long busy)
   {
      System.out.println("Starting operation");
      System.out.println("Current thread [id=" + Thread.currentThread().getId() + "; name=" + Thread.currentThread().getName() + "]");
      super.fire(busy);
      System.out.println("Operation completed");
      threadValue.set(Thread.currentThread().getId());
      LATCH.countDown();
   }
}
