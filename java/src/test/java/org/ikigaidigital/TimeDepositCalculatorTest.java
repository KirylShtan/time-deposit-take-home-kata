package org.ikigaidigital;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimeDepositCalculatorTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;


    private double balanceAfter(String planType,double balance,int days) {
        TimeDeposit deposit = new TimeDeposit(1,planType,balance,days);
        new TimeDepositCalculator().updateBalance(List.of(deposit));
        return deposit.getBalance();
    }
    @Test
    @Order(1)
    void getReturnsSeededDeposits() throws Exception {
        mockMvc.perform(get("/time-deposits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[?(@.id == 1)].planType").value("basic"))
                .andExpect(jsonPath("$[?(@.id == 1)].days").value(31))
                .andExpect(jsonPath("$[?(@.id == 1)].balance").value(1000.0))
                .andExpect(jsonPath("$[?(@.id == 1)].withdrawals.length()").value(1))
                .andExpect(jsonPath("$[?(@.id == 4)].planType").value("premium"))
                .andExpect(jsonPath("$[?(@.id == 4)].days").value(45));
    }

    @Test
    @Order(2)
    void postAppliesInterestAndKeepsDays() throws Exception {
        mockMvc.perform(post("/time-deposits/balance-updates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1)].balance").value(1000.83))
                .andExpect(jsonPath("$[?(@.id == 1)].days").value(31))
                .andExpect(jsonPath("$[?(@.id == 2)].balance").value(1002.5))
                .andExpect(jsonPath("$[?(@.id == 3)].balance").value(1004.17))
                .andExpect(jsonPath("$[?(@.id == 4)].balance").value(1000.0))
                .andExpect(jsonPath("$[?(@.id == 4)].days").value(45));
        mockMvc.perform(get("/time-deposits"))
                .andExpect(jsonPath("$[?(@.id == 1)].balance").value(1000.83));
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
