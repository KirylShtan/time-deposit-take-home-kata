package org.ikigaidigital.adapter.web;

import org.ikigaidigital.domain.Withdrawal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WithdrawalResponse(int id , int timeDepositId, BigDecimal amount, LocalDate date) {

    static WithdrawalResponse from(Withdrawal withdrawal) {
        return new WithdrawalResponse(
                withdrawal.id(),  withdrawal.timeDepositId(), withdrawal.amount(), withdrawal.date()
        );
    }
}
