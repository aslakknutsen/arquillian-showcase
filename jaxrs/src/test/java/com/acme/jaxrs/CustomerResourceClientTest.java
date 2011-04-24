package com.acme.jaxrs;

import java.net.URL;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.jboss.arquillian.api.ArquillianResource;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.persistence.EntityManagerProducer;
import com.acme.jaxrs.resource.CustomerResource;
import com.acme.jaxrs.rs.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public class CustomerResourceClientTest
{
   private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);

   @Deployment
   public static WebArchive createDeployment() 
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(Customer.class.getPackage())
            .addClasses(EntityManagerProducer.class, CustomerResource.class)
            //.addAsManifestResource("test-persistence.xml", "persistence.xml")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("import.sql")
            .addClass(JaxRsActivator.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @ArquillianResource
   URL deploymentUrl;

   @Test
   public void testGetCustomerByIdUsingClientRequest() throws Exception 
   {
      // GET http://localhost:8080/test/rest/customer/1
      ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/customer/1");
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
