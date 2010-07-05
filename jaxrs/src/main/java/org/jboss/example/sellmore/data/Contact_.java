package org.jboss.example.sellmore.data;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Contact.class)
public abstract class Contact_ {

	public static volatile SingularAttribute<Contact, Long> id;
	public static volatile SingularAttribute<Contact, String> phone;
	public static volatile SingularAttribute<Contact, String> address;
	public static volatile SingularAttribute<Contact, Customer> customer;
	public static volatile SingularAttribute<Contact, String> city;

}

