package org.ikigaidigital.application;

import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.TimeDepositCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TimeDepositService {

    private final TimeDepositRepository repository;

    public TimeDepositService(TimeDepositRepository repository) {
        this.repository = repository;
    }
    public List<DepositWithWithdrawals> findAll(){
        return repository.findAll();
    }
    public List<DepositWithWithdrawals> updateBalances() {
        List<DepositWithWithdrawals> deposits = repository.findAll();
        List<TimeDeposit> plans = deposits.stream()
                .map(DepositWithWithdrawals::deposit)
                .toList();
        new TimeDepositCalculator().updateBalance(plans);
        repository.updateBalances(plans);
        return deposits;
    }
}
