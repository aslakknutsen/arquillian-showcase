package com.acme.jaxrs;

import static org.jboss.arquillian.api.RunModeType.AS_CLIENT;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.persistence.EntityManagerProducer;
import com.acme.jaxrs.resource.CustomerResource;

@RunWith(Arquillian.class)
@Run(AS_CLIENT)
public class CustomerResourceClientTest
{
   private static final String REST_URI = "http://localhost:8080/test/rest";

   @Deployment
   public static Archive<?> createDeployment() 
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(Customer.class.getPackage())
            .addClasses(EntityManagerProducer.class, CustomerResource.class)
            .addManifestResource("test-persistence.xml", "persistence.xml")
            .addWebResource("import.sql", "classes/import.sql")
            
            // enable to activate JAX-RS 1.1 on compliant containers
            //.addClass(JaxRsConfiguration.class)
            
            // temporarily required for RESTEasy JAX-RS 1.1 compliance in JBoss AS 6.0.0 M4
            .addDirectory("WEB-INF/lib")
            .setWebXML("test-web.xml")
         
            .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void testGetCustomerByIdUsingClientRequest() throws Exception 
   {
      // GET http://localhost:8080/test/rest/customer/1
      ClientRequest request = new ClientRequest(REST_URI + "/customer/1");
      request.header("Accept", MediaType.APPLICATION_XML);

      // we're expecting a String back
      ClientResponse<String> responseObj = request.get(String.class);

      Assert.assertEquals(200, responseObj.getStatus());
      System.out.println("GET /customer/1 HTTP/1.1\n\n" + responseObj.getEntity());
      
      Assert.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      		"<customer><id>1</id><name>Acme Corporation</name></customer>", 
      		responseObj.getEntity());
   }
}
