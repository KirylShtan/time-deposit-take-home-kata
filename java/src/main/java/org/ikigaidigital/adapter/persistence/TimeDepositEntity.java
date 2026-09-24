package org.ikigaidigital.adapter.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"timeDeposits\"")
@Getter
public class TimeDepositEntity {
    @Id
    private Integer id;

    @Column(name = "\"planType\"", nullable = false)
    private String planType;

    @Column(nullable = false)
    private Integer days;

    @Setter
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "timeDeposit")
    private List<WithdrawalEntity> withDrawals = new ArrayList<>();

}
