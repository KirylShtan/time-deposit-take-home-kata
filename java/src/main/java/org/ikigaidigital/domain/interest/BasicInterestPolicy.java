package org.ikigaidigital.domain.interest;

import org.ikigaidigital.TimeDeposit;

public class BasicInterestPolicy implements InterestPolicy {
    @Override
    public boolean supports(String planType) {
        return "basic".equals(planType);
    }

    @Override
    public double interest(TimeDeposit deposit) {
        if (deposit.getDays() <= 30){
            return 0.0;
        }
        return deposit.getBalance()* 0.01/12;
    }
}
