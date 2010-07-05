package org.jboss.example.sellmore.data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SalesOrder.class)
public abstract class SalesOrder_ {

	public static volatile SingularAttribute<SalesOrder, BigDecimal> amount;
	public static volatile SingularAttribute<SalesOrder, Long> id;
	public static volatile SingularAttribute<SalesOrder, Date> created;
	public static volatile SingularAttribute<SalesOrder, Customer> customer;
	public static volatile SetAttribute<SalesOrder, LineItem> lineItems;

}

