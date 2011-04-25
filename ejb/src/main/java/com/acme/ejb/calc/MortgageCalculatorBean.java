package com.acme.ejb.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class MortgageCalculatorBean implements MortgageCalculator {
    private static final BigDecimal TWELVE = new BigDecimal(12);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final int CURRENCY_DECIMALS = 2;

    @Resource(name = "interestRate")
    private Double interestRate;

    /**
     * a = [ P(1 + r)^Y * r ] / [ (1 + r)^Y - 1 ]
     */
    @Override
    public BigDecimal calculateMonthlyPayment(double principal, double interestRate, int termYears) {
        // System.out.println(getClass().getName() + ".calculateMonthlyPayment() call chain:");
        // for (StackTraceElement e : Thread.currentThread().getStackTrace())
        // {
        // if (!e.getClassName().equals(getClass().getName()) &&
        // !e.getMethodName().equals("getStackTrace") &&
        // !(e.getLineNumber() < -1)) {
        // System.out.println("  L " + e.getClassName() + "." + e.getMethodName() + "(line: " + e.getLineNumber() + ")");
        // }
        // }
        BigDecimal p = new BigDecimal(principal);
        int divisionScale = p.precision() + CURRENCY_DECIMALS;
        BigDecimal r = new BigDecimal(interestRate).divide(ONE_HUNDRED, MathContext.UNLIMITED).divide(TWELVE, divisionScale,
                RoundingMode.HALF_EVEN);
        BigDecimal z = r.add(BigDecimal.ONE);
        BigDecimal tr = new BigDecimal(Math.pow(z.doubleValue(), termYears * 12));
        return p.multiply(tr).multiply(r).divide(tr.subtract(BigDecimal.ONE), divisionScale, RoundingMode.HALF_EVEN)
                .setScale(CURRENCY_DECIMALS, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateMonthlyPayment(double principal, int termYears) {
        if (interestRate == null) {
            throw new IllegalStateException("No interest rate has been specified.");
        }
        return calculateMonthlyPayment(principal, interestRate, termYears);
    }

    @Override
    public double getCurrentInterestRate() {
        if (interestRate == null) {
            throw new IllegalStateException("No interest rate has been specified.");
        }
        return interestRate;
    }

}
