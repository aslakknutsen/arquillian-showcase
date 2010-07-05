package org.jboss.example.sellmore.data;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, Long> id;
	public static volatile SingularAttribute<Customer, String> name;
	public static volatile SetAttribute<Customer, SalesOrder> orders;
	public static volatile SetAttribute<Customer, Contact> contacts;

}

