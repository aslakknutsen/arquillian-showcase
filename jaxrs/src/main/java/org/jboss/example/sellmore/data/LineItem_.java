package org.jboss.example.sellmore.data;

import java.math.BigDecimal;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(LineItem.class)
public abstract class LineItem_ {

	public static volatile SingularAttribute<LineItem, Product> product;
	public static volatile SingularAttribute<LineItem, Long> id;
	public static volatile SingularAttribute<LineItem, SalesOrder> order;
	public static volatile SingularAttribute<LineItem, BigDecimal> quantity;

}

