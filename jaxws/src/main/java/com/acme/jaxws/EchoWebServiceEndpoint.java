package com.acme.jaxws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Echo web service endpoint interface
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
@WebService
@SOAPBinding
public interface EchoWebServiceEndpoint {

    String echo(String s);

}
