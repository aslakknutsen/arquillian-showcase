package org.jboss.example.sellmore;

import static org.jboss.arquillian.api.RunModeType.AS_CLIENT;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.example.sellmore.data.Customer;
import org.jboss.example.sellmore.persistence.EntityManagerProducer;
import org.jboss.example.sellmore.resource.CustomerResource;
import org.jboss.example.sellmore.rs.JaxRsConfig;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Run(AS_CLIENT)
public class CustomerResourceClientTest
{
   private static final String REST_URI = "http://localhost:8080/test/rest";

   @Deployment
   public static WebArchive createTestArchive() 
   {
      return ShrinkWrap.create("test.war", WebArchive.class)
         .addPackages(true, Customer.class.getPackage())
         .addClasses(JaxRsConfig.class, EntityManagerProducer.class, CustomerResource.class)
         .addWebResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml")
         .addWebResource("import.sql", "classes/import.sql")
         .addWebResource(new ByteArrayAsset(new byte[0]), "beans.xml")
         // temporarily required for restEasy Jax_RS 1.1 compliance in JBoss AS 6.0 M3
         .addLibraries(
            ArtifactResolver.resolve("org.jboss.resteasy:resteasy-cdi:2.0-beta-3-SNAPSHOT"))
         .setWebXML("WEB-INF/test-web.xml");
   }

   @Test
   public void testGetCustomerByIdManually() throws Exception 
   {
      // GET http://localhost:8080/test/rest/customer/1
      ClientRequest request = new ClientRequest(REST_URI + "/customer/1");
      request.header("Accept", MediaType.APPLICATION_XML);

      // we're expecting a String back
      ClientResponse<String> responseObj = request.get(String.class);

      Assert.assertEquals(200, responseObj.getStatus());
      System.out.println("----------\n" + responseObj.getEntity() + "\n----------");
      
      Assert.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      		"<customer><id>1</id><name>Acme Corporation</name></customer>", 
      		responseObj.getEntity());
   }

   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

   
 
// @BeforeClass
// public static void initResteasyClient() {
//   RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
// }

   
// @Test
// public void testGetCustomerByIdWithClientProxy() throws Exception
// {
//    // GET http://localhost:8080/test/rest/customer/1
//    CustomerClient client = ProxyFactory.create(CustomerClient.class, REST_URI);
//    String response = client.getCustomerById(1);
//    
//    assertNotNull(response);
//    
//    System.out.println("----------\n" + response + "\n----------");
//    
//    response = response.replaceAll("<\\?xml.*\\?>", "").trim();
//    assertEquals("<customer><id>1</id><name>Acme Corporation</name></customer>", response);
// }


}
 