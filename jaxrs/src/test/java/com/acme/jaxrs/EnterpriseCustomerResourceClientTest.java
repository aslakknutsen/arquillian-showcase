package com.acme.jaxrs;

import java.net.URL;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.persistence.EntityManagerProducer;
import com.acme.jaxrs.resource.CustomerResource;
import com.acme.jaxrs.rs.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
@Ignore
public class EnterpriseCustomerResourceClientTest {
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);

    @Deployment(testable = false)
    public static EnterpriseArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class).addPackage(Customer.class.getPackage())
                .addClasses(EntityManagerProducer.class, CustomerResource.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsResource("import.sql")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war").addClass(JaxRsActivator.class);
        
        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsModule(jar).addAsModule(war);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testGetCustomerByIdUsingClientRequest() throws Exception {
        // GET http://localhost:8080/test/rest/customer/1
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/customer/1");
        request.header("Accept", MediaType.APPLICATION_XML);

        // we're expecting a String back
        ClientResponse<String> responseObj = request.get(String.class);

        Assert.assertEquals(200, responseObj.getStatus());
        System.out.println("GET /customer/1 HTTP/1.1\n\n" + responseObj.getEntity());

        String response = responseObj.getEntity().replaceAll("<\\?xml.*\\?>", "").trim();
        Assert.assertEquals("<customer><id>1</id><name>Acme Corporation</name></customer>", response);
    }
}
