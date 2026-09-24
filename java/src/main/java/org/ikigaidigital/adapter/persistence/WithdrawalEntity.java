package org.ikigaidigital.adapter.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "withdrawals")
public class WithdrawalEntity {

    @Id
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"timeDepositId\"", nullable = false )
    private TimeDepositEntity timeDeposit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

}
