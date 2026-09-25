package org.ikigaidigital.adapter.web;

import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.application.DepositWithWithdrawals;

import java.util.List;

public record TimeDepositResponse(int id, String planType, double balance, int days, List<WithdrawalResponse> withdrawals) {

    static TimeDepositResponse from(DepositWithWithdrawals source) {
        TimeDeposit deposit = source.deposit();
        return new TimeDepositResponse(
                deposit.getId(),
                deposit.getPlanType(),
                deposit.getBalance(),
                deposit.getDays(),
                source.withdrawals().stream().map(WithdrawalResponse::from)
                        .toList()
        );
    }
}
