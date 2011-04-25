package com.acme.jpa;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RECORD")
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Set<LineItem> lineItems;

    public Record() {
    }

    public Record(String name) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    public Set<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public void addLineItem(LineItem e) {
        if (lineItems == null) {
            lineItems = new HashSet<LineItem>();
        }
        lineItems.add(e);
        e.setRecord(this);
    }

    @Override
    public String toString() {
        return "Record@" + hashCode() + "[id = " + id + "; name = " + name + "]";
    }
}
