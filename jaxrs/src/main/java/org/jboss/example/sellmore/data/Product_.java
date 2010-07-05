package org.jboss.example.sellmore.data;

import java.math.BigDecimal;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, BigDecimal> unitPrice;
	public static volatile SingularAttribute<Product, String> code;

}

