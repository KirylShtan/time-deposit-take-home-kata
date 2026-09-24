package org.ikigaidigital.application;

import org.ikigaidigital.TimeDeposit;

import java.util.List;

public interface TimeDepositRepository {
    List<DepositWithWithdrawals> findAll();
    void updateBalances(List<TimeDeposit> deposits);
}
