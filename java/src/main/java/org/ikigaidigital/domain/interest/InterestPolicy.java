package org.ikigaidigital.domain.interest;

import org.ikigaidigital.TimeDeposit;

public interface InterestPolicy {
    boolean supports(String planType);
    double interest(TimeDeposit deposit);
}
