package com.acme.ejb.calc;

import java.math.BigDecimal;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MortgageCalculatorEnvEntryTestCase {
    @Deployment
    public static Archive<?> createDeployment() {
        // we have to create a war because ejb-jar.xml must be put in WEB-INF
        // explicit archive name required until ARQ-77 is resolved
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(MortgageCalculator.class, MortgageCalculatorBean.class)
                .addAsWebInfResource("interest-rate-ejb-jar.xml", "ejb-jar.xml");
        // return ShrinkWrap.create(JavaArchive.class)
        // .addClasses(MortgageCalculator.class, MortgageCalculatorBean.class)
        // .addAsManifestResource("interest-rate-ejb-jar.xml", "ejb-jar.xml");
    }

    @EJB
    MortgageCalculator calculator;

    @Test
    public void shouldCalculateMonthlyPaymentAccuratelyWithBuiltInRate() {
        Assert.assertEquals("Interest rate should be set by ejb-jar.xml", 5.5, calculator.getCurrentInterestRate());

        double principal = 750000;
        int term = 30;
        BigDecimal expected = new BigDecimal(Double.toString(4258.42));

        BigDecimal actual = calculator.calculateMonthlyPayment(principal, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);
    }
}
