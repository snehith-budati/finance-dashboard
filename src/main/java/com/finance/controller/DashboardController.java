package com.finance.controller;

import com.finance.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, BigDecimal>> getSummary() {
        BigDecimal income = dashboardService.getTotalIncome();
        BigDecimal expenses = dashboardService.getTotalExpenses();
        BigDecimal balance = dashboardService.getNetBalance();

        return ResponseEntity.ok(Map.of(
                "totalIncome", income,
                "totalExpenses", expenses,
                "netBalance", balance
        ));
    }
}
