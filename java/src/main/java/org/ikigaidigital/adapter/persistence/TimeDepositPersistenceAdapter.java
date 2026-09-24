package org.ikigaidigital.adapter.persistence;

import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.application.DepositWithWithdrawals;
import org.ikigaidigital.application.TimeDepositRepository;
import org.ikigaidigital.domain.Withdrawal;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Component
public class TimeDepositPersistenceAdapter implements TimeDepositRepository {
    private final TimeDepositJpaRepository jpaRepository;

    public TimeDepositPersistenceAdapter(TimeDepositJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DepositWithWithdrawals> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }
    @Transactional
    @Override
    public void updateBalances(List<TimeDeposit> deposits) {
        for (TimeDeposit deposit : deposits) {
            jpaRepository.findById(deposit.getId()).ifPresent(entity -> {
                entity.setBalance(BigDecimal.valueOf(deposit.getBalance()));
                jpaRepository.save(entity);
            });
        }
    }
    private DepositWithWithdrawals toDomain(TimeDepositEntity entity) {
        TimeDeposit deposit = new TimeDeposit(
                entity.getId(),
                entity.getPlanType(),
                entity.getBalance().doubleValue(),
                entity.getDays()
        );
        List<Withdrawal> withdrawals = entity.getWithDrawals().stream()
                .map(withdrawal -> new Withdrawal(
                        withdrawal.getId(),
                        entity.getId(),
                        withdrawal.getAmount(),
                        withdrawal.getDate()
                )).toList();
        return new DepositWithWithdrawals(deposit, withdrawals);
    }
}
