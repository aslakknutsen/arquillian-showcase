package com.acme.jaxrs;

import static org.jboss.arquillian.api.RunModeType.AS_CLIENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.junit.Arquillian;
import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.persistence.EntityManagerProducer;
import com.acme.jaxrs.resource.CustomerClient;
import com.acme.jaxrs.resource.CustomerResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Run(AS_CLIENT)
public class CustomerResourceRESTEasyClientTest
{
   private static final String REST_URI = "http://localhost:8080/test/rest";

   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(Customer.class.getPackage())
            .addClasses(EntityManagerProducer.class, CustomerResource.class)
            .addWebResource("test-persistence.xml", "classes/META-INF/persistence.xml")
            .addWebResource("import.sql", "classes/import.sql")

            // enable to activate JAX-RS 1.1 on compliant containers
            //.addClass(JaxRsConfiguration.class)

            // temporarily required for RESTEasy JAX-RS 1.1 compliance in JBoss AS 6.0.0 M4
            .addDirectory("WEB-INF/lib")
            .setWebXML("test-web.xml")

            .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @BeforeClass
   public static void initResteasyClient()
   {
      RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
   }

   @Test
   public void testGetCustomerByIdUsingClientProxy() throws Exception
   {
      // GET http://localhost:8080/test/rest/customer/1
      CustomerClient client = ProxyFactory.create(CustomerClient.class, REST_URI);
      String response = client.getCustomerById(1);

      assertNotNull(response);

      System.out.println("GET /customer/1 HTTP/1.1\n\n" + response);

      response = response.replaceAll("<\\?xml.*\\?>", "").trim();
      assertEquals("<customer><id>1</id><name>Acme Corporation</name></customer>", response);
   }
}
