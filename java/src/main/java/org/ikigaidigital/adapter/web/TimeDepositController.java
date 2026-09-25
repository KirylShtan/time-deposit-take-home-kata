package org.ikigaidigital.adapter.web;

import org.ikigaidigital.application.TimeDepositService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
