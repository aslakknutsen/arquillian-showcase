package com.acme.jaxrs.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A <strong>Customer</strong> is an entity for whom orders are created.
 */
@Entity
@XmlRootElement
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Set<Contact> contacts;
    private String name;
    private Set<SalesOrder> orders;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    @OneToMany(mappedBy = "customer")
    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    @NotNull
    @Size(min = 3, max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    @OneToMany(mappedBy = "customer")
    public Set<SalesOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<SalesOrder> orders) {
        this.orders = orders;
    }
}
