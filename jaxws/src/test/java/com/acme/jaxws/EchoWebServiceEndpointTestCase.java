package com.acme.jaxws;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests simple stateless web service endpoint invocation.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
@RunWith(Arquillian.class)
public class EchoWebServiceEndpointTestCase {

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        
        return ShrinkWrap.create(WebArchive.class, "jaxws-endpoint-test.war")
                .addPackage(EchoWebServiceEndpointBean.class.getPackage())
                .setWebXML(new StringAsset(webXml
                        .servlet("EchoService", EchoWebServiceEndpointBean.class.getName(), new String[] { "/EchoService" })
                        .exportAsString()));
    }

    @Test
    public void testSimpleStatelessWebServiceEndpoint() throws Exception {
        final QName serviceName = new QName("com.acme.jaxws", "EchoService");
        final URL wsdlURL = new URL("http://localhost:8080/jaxws-endpoint-test/EchoService?wsdl");
        final Service service = Service.create(wsdlURL, serviceName);
        final EchoWebServiceEndpoint port = service.getPort(EchoWebServiceEndpoint.class);
        final String result = port.echo("hello");
        Assert.assertEquals("hello", result);
    }

}
