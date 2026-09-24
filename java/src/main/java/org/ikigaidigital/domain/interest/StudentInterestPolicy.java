package org.ikigaidigital.domain.interest;

import org.ikigaidigital.TimeDeposit;

public class StudentInterestPolicy implements InterestPolicy {
    @Override
    public boolean supports(String planType) {
        return "student".equals(planType);
    }

    @Override
    public double interest(TimeDeposit deposit) {
        if (deposit.getDays() <= 365 && deposit.getDays() > 30) {
            return deposit.getBalance()* 0.03/12;
        }
        return 0.0;
    }
}
