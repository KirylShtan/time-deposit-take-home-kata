package org.ikigaidigital;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeDepositCalculatorTest {
    private double balanceAfter(String planType,double balance,int days) {
        TimeDeposit deposit = new TimeDeposit(1,planType,balance,days);
        new TimeDepositCalculator().updateBalance(List.of(deposit));
        return deposit.getBalance();
    }
    @Test
    void basicEarnsOnePercentAfter30Days() {
        assertThat(balanceAfter("basic", 1000.0, 31)).isEqualTo(1000.83);
    }
    @Test
    void noPlanEarnsInterestOnDay30(){
        assertThat(balanceAfter("basic", 1000.0, 30)).isEqualTo(1000.0);
        assertThat(balanceAfter("student", 1000.0, 30)).isEqualTo(1000.0);
        assertThat(balanceAfter("premium", 1000.0, 30)).isEqualTo(1000.0);
    }
    @Test
    void premiumStartsAfter45Days(){
        assertThat(balanceAfter("premium", 1000.0, 45)).isEqualTo(1000.0);
        assertThat(balanceAfter("premium", 1000.0, 46)).isEqualTo(1004.17);
    }
    @Test
    void studentStopsAfterOneYear(){
        assertThat(balanceAfter("student", 1000.0, 365)).isEqualTo(1002.5);
        assertThat(balanceAfter("student", 1000.0, 366)).isEqualTo(1000.0);
    }

    @Test
    void updateBalanceKeepsIdentityAndUpdatesEveryDeposit(){
        TimeDeposit basic = new TimeDeposit(1,"basic",1000.0,31);
        TimeDeposit premium = new TimeDeposit(1,"premium",1000.0,46);
        new TimeDepositCalculator().updateBalance(List.of(basic,premium));

        assertThat(basic.getId()).isEqualTo(1);
        assertThat(basic.getPlanType()).isEqualTo("basic");
        assertThat(basic.getDays()).isEqualTo(31);
        assertThat(basic.getBalance()).isEqualTo(1000.83);
        assertThat(premium.getBalance()).isEqualTo(1004.17);

    }

}
