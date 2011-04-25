package com.acme.jaxrs.resource;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.acme.jaxrs.model.Customer;

/**
 * A REST service for retrieving Customer records
 */
@ManagedBean
@Path("/customer")
public class CustomerResource {
    @Inject
    private EntityManager em;

    @GET
    public List<Customer> getAllCustomers() {
        return findAllCustomers();
    }

    /**
     * This method responds to a GET request that supports the content type application/xml or application/json and returns the
     * requested customer resource.
     * 
     * <p>
     * The customer is retrieved by id. A representation of the customer is then written into the response in the requested
     * format. The id value is taken from the final path segment.
     * </p>
     * 
     * <a class="citation" href= "javacode://com.acme.jaxrs.CustomerResource#getCustomerById(java.lang.String)" />
     */
    @GET
    @Path("/{id:[1-9][0-9]*}")
    public Customer getCustomerById(@PathParam("id") long id) {
        System.out.println("Handling resource request for /customer/" + id);
        return findCustomerById(id);
    }

    /**
     * This method responds to a GET request that supports the content type application/xml or application/json and returns the
     * requested customer resource.
     * 
     * <p>
     * The customer is retrieved by name. A representation of the customer is then written into the response in the requested
     * format. The name value is taken from the final path segment.
     * </p>
     * 
     * <a class="citation" href= "javacode://com.acme.jaxrs.CustomerResource#getCustomerByName(java.lang.String)" />
     */
    @GET
    @Path("/{name}")
    public Customer getCustomerByName(@PathParam("name") String name) {
        return findCustomerByName(name);
    }

    @GET
    @Path("/{id:[1-9][0-9]*}")
    @Produces("text/html")
    public String getCustomerHtml(@PathParam("id") long id) {
        Customer c = findCustomerById(id);
        if (c != null) {
            StringBuilder html = new StringBuilder();
            html.append("<html><head><title>");
            html.append(c.getName());
            html.append("</title></head><body><h1>");
            html.append(c.getName());
            html.append("</h1><dl><dt>Id</dt><dd>");
            html.append(c.getId());
            html.append("</dd><dt>Name</dt><dd>");
            html.append(c.getName());
            html.append("</dd></dl></body>");
            return html.toString();
        }
        return null;
    }

    public List<Customer> findAllCustomers() {
        CriteriaQuery<Customer> criteria = em.getCriteriaBuilder().createQuery(Customer.class);
        criteria.from(Customer.class);
        return em.createQuery(criteria).getResultList();
    }

    public Customer findCustomerByName(String name) {
        if (name == null) {
            return null;
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        // Toggle comment on first equal criteria below (and comment the subsequent line)
        // if you want to try out type-safe criteria queries, a new feature in JPA 2.0
        // requires that the metamodel generator is configured correctly
        // criteria.select(customer).where(cb.equal(customer.get(Customer_.name), name));
        criteria.select(customer).where(cb.equal(customer.get("name"), name));
        return (Customer) em.createQuery(criteria).setMaxResults(1).getSingleResult();
    }

    public Customer findCustomerById(long id) {
        return em.find(Customer.class, id);
    }
}
