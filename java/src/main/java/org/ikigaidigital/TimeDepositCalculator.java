package org.ikigaidigital;

import org.ikigaidigital.domain.interest.BasicInterestPolicy;
import org.ikigaidigital.domain.interest.InterestPolicy;
import org.ikigaidigital.domain.interest.PremiumInterestPolicy;
import org.ikigaidigital.domain.interest.StudentInterestPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TimeDepositCalculator {
    private final List<InterestPolicy> policies = List.of(
            new StudentInterestPolicy(),
            new BasicInterestPolicy(),
            new PremiumInterestPolicy()
    );
    public void updateBalance(List<TimeDeposit> xs) {
        for (int i = 0; i < xs.size(); i++) {
            TimeDeposit deposit = xs.get(i);
            double interest = policies.stream()
                    .filter(policy -> policy.supports(deposit.getPlanType()))
                    .findFirst()
                    .map(policy -> policy.interest(deposit))
                    .orElse(0.0);

            double rounded = new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).doubleValue();
            deposit.setBalance(deposit.getBalance() + rounded);
        }
    }
}
