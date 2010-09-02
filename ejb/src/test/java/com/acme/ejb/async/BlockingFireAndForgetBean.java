package com.acme.ejb.async;

import java.util.concurrent.CountDownLatch;

import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(BlockingFireAndForget.class)
public class BlockingFireAndForgetBean extends AbstractFireAndForgetBean implements BlockingFireAndForget
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
