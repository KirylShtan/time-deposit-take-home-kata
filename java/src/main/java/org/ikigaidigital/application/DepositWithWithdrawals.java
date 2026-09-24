package org.ikigaidigital.application;

import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.domain.Withdrawal;

import java.util.List;

public record DepositWithWithdrawals(TimeDeposit deposit, List<Withdrawal> withdrawals ) {
}
