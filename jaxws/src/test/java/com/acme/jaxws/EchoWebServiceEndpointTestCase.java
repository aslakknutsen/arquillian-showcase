package com.acme.jaxws;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests simple stateless web service endpoint invocation.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@RunWith(Arquillian.class)
public class EchoWebServiceEndpointTestCase {

    private static final String ECHO_SERVICE_NAME = "EchoService";
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        
        return ShrinkWrap.create(WebArchive.class, "jaxws-endpoint-test.war")
                .addPackage(EchoWebServiceEndpointBean.class.getPackage())
                .setWebXML(new StringAsset(webXml.createServlet()
                        .servletName(ECHO_SERVICE_NAME).servletClass(EchoWebServiceEndpointBean.class.getName()).up()
                        .createServletMapping().servletName(ECHO_SERVICE_NAME).urlPattern("/" + ECHO_SERVICE_NAME).up()
                        .exportAsString()));
    }

    @Test
    public void testSimpleStatelessWebServiceEndpoint(@ArquillianResource URL deploymentUrl) throws Exception {
        final QName serviceName = new QName("com.acme.jaxws", ECHO_SERVICE_NAME);
        final URL wsdlURL = new URL(deploymentUrl, ECHO_SERVICE_NAME + "?wsdl");
        final Service service = Service.create(wsdlURL, serviceName);
        final EchoWebServiceEndpoint port = service.getPort(EchoWebServiceEndpoint.class);
        final String result = port.echo("hello");
        Assert.assertEquals("hello", result);
    }

}
