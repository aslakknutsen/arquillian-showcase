package com.acme.cdiejb.async;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FireAndForgetUsingSpecializedBeanTestCase {
    
    // use on compatible containers
    //@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(FireAndForget.class, FireAndForgetBean.class, BlockingFireAndForgetSpecializedBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // use on GlassFish because of a visibility bug
    @Deployment
    public static WebArchive createDeploymentForGlassFish() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(FireAndForget.class, FireAndForgetBean.class, BlockingFireAndForgetSpecializedBean.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    FireAndForget asyncBean;

    @Test
    public void shouldInvokeAsynchronously() throws Exception {
        // use latch barrier or something to verify code is executing asynchronously
        System.out.println("Current thread [id=" + Thread.currentThread().getId() + "; name=" + Thread.currentThread().getName() + "]");
        asyncBean.fire(1000);
        System.out.println("Async operation fired");
        assertTrue("Async operation completed in given time", BlockingFireAndForgetSpecializedBean.LATCH.await(30, TimeUnit.SECONDS));
        System.out.println("Async thread complete");
        // Thread local value in this thread should remain unset after the async invocation
        assertNull("Async operation correctly cleaned up", BlockingFireAndForgetSpecializedBean.threadValue.get());
    }
}
