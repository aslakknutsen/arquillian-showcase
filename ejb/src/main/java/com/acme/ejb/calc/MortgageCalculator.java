package com.acme.ejb.calc;

import java.math.BigDecimal;
import javax.ejb.Local;

@Local
public interface MortgageCalculator {
    BigDecimal calculateMonthlyPayment(double principal, double interestRate, int termYears);

    BigDecimal calculateMonthlyPayment(double principal, int termYears);

    double getCurrentInterestRate();
}
