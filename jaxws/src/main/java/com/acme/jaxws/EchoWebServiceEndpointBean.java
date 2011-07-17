package com.acme.jaxws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 * Echo web service endpoint implementation.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
@WebService(
        endpointInterface = "com.acme.jaxws.EchoWebServiceEndpoint",
        targetNamespace = "com.acme.jaxws",
        serviceName = "EchoService"
)
public class EchoWebServiceEndpointBean { //implements EchoWebServiceEndpoint {

    @Resource
    private WebServiceContext ctx;

    public String echo(final String s) {
        if (ctx == null) {
            throw new RuntimeException("@Resource WebServiceContext not injected");
        }
        
        return s;
    }

}
