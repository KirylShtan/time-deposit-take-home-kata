package org.ikigaidigital.application;

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
}
