package org.ikigaidigital.adapter.web;

import org.ikigaidigital.application.TimeDepositService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeDepositController {
    private TimeDepositService timeDepositService;
    public TimeDepositController(TimeDepositService timeDepositService) {
        this.timeDepositService = timeDepositService;
    }
    @GetMapping("/time-deposits")
    public List<TimeDepositResponse> getTimeDeposits() {
        return timeDepositService.findAll().stream()
                .map(TimeDepositResponse::from)
                .toList();
    }
    @PostMapping("/time-deposits/balance-updates")
    public List<TimeDepositResponse> updateBalances() {
        return timeDepositService.updateBalances().stream()
                .map(TimeDepositResponse::from)
                .toList();
    }
}
