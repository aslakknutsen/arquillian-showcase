package com.acme.jaxrs.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public interface CustomerClient {
    @GET
    @Path("/customer")
    @Produces("application/xml")
    String getCustomers();

    @GET
    @Path("/customer/{id}")
    @Produces("application/xml")
    String getCustomerById(@PathParam("id") long id);
}
