package com.acme.ejb.calc;

import java.math.BigDecimal;
import javax.ejb.EJB;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MortgageCalculatorTestCase {
    @Deployment
    public static JavaArchive createDeployment() {
        // explicit archive name required until ARQ-77 is resolved
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(MortgageCalculator.class, MortgageCalculatorBean.class);
    }

    @EJB
    MortgageCalculator calculator;

    @Test
    public void shouldCalculateMonthlyPaymentAccurately() {
        // calculator = new MortgageCalculatorBean();

        double principal = 750000;
        double rate = 7.5;
        int term = 30;
        BigDecimal expected = new BigDecimal(Double.toString(5244.11));

        BigDecimal actual = calculator.calculateMonthlyPayment(principal, rate, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);

        principal = 2500000;
        rate = 5.5;
        term = 30;
        expected = new BigDecimal(Double.toString(14194.72));

        actual = calculator.calculateMonthlyPayment(principal, rate, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);
    }
}
