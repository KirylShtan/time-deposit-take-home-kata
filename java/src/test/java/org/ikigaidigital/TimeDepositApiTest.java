package org.ikigaidigital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TimeDepositApiTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void reseed() {
        jdbcTemplate.update("DELETE FROM withdrawals");
        jdbcTemplate.update("DELETE FROM \"timeDeposits\"");
        jdbcTemplate.update("""
                INSERT INTO "timeDeposits" (id, "planType", days, balance) VALUES
                    (1, 'basic', 31, 1000.00),
                    (2, 'student', 365, 1000.00),
                    (3, 'premium', 46, 1000.00),
                    (4, 'premium', 45, 1000.00)
                """);
        jdbcTemplate.update("""
                INSERT INTO withdrawals (id, "timeDepositId", amount, date) VALUES
                    (1, 1, 100.00, DATE '2026-01-15'),
                    (2, 2, 50.00, DATE '2026-02-01')
                """);
    }

    @Test
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
}
