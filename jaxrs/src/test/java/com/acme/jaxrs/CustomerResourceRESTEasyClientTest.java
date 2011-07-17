package com.acme.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.ws.rs.ApplicationPath;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.persistence.EntityManagerProducer;
import com.acme.jaxrs.resource.CustomerClient;
import com.acme.jaxrs.resource.CustomerResource;
import com.acme.jaxrs.rs.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public class CustomerResourceRESTEasyClientTest {
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Customer.class.getPackage())
                .addClasses(EntityManagerProducer.class, CustomerResource.class, JaxRsActivator.class)
                // .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;

    @BeforeClass
    public static void initResteasyClient() {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    @Test
    public void testGetCustomerByIdUsingClientProxy() throws Exception {
        CustomerClient client = ProxyFactory.create(CustomerClient.class, deploymentUrl.toString() + RESOURCE_PREFIX);
        // GET http://localhost:8080/test/rest/customer/1
        String response = client.getCustomerById(1);

        assertNotNull(response);

        System.out.println("GET /customer/1 HTTP/1.1\n\n" + response);

        response = response.replaceAll("<\\?xml.*\\?>", "").trim();
        assertEquals("<customer><id>1</id><name>Acme Corporation</name></customer>", response);
    }
}
