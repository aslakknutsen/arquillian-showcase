package com.acme.jaxrs.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.acme.jaxrs.model.Customer;

@Path("/customer/{id:[1-9][0-9]*}")
public class CustomerResourceStub {
    @GET
    public Customer getCustomer(@PathParam("id") long id) {
        Customer c = new Customer("Acme");
        c.setId(1l);
        return c;
    }
}
