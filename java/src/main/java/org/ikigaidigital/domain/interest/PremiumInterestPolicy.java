package org.ikigaidigital.domain.interest;

import org.ikigaidigital.TimeDeposit;

public class PremiumInterestPolicy implements InterestPolicy{

    @Override
    public boolean supports(String planType) {
        return "premium".equals(planType);
    }

    @Override
    public double interest(TimeDeposit deposit) {
        if (deposit.getDays() <= 45){
            return 0.0;
        }
        return deposit.getBalance()* 0.05/12;
    }
}
