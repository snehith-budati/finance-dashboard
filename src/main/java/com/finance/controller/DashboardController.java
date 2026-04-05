package com.finance.controller;

import com.finance.entity.FinancialRecord;
import com.finance.service.AuthorizationService;
import com.finance.service.DashboardService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthorizationService authorizationService;

    public DashboardController(DashboardService dashboardService, AuthorizationService authorizationService) {
        this.dashboardService = dashboardService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, BigDecimal>> getSummary(@RequestHeader("X-User-Id") Long actorUserId) {
        authorizationService.requireViewer(actorUserId);

        BigDecimal income = dashboardService.getTotalIncome();
        BigDecimal expenses = dashboardService.getTotalExpenses();
        BigDecimal balance = dashboardService.getNetBalance();

        return ResponseEntity.ok(Map.of(
                "totalIncome", income,
                "totalExpenses", expenses,
                "netBalance", balance
        ));
    }

    @GetMapping("/category-totals")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryTotals(@RequestHeader("X-User-Id") Long actorUserId) {
        authorizationService.requireViewer(actorUserId);
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyTrends(@RequestHeader("X-User-Id") Long actorUserId) {
        authorizationService.requireViewer(actorUserId);
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<FinancialRecord>> getRecentActivity(
            @RequestHeader("X-User-Id") Long actorUserId,
            @RequestParam(defaultValue = "5") @Min(1) @Max(100) int limit) {
        authorizationService.requireViewer(actorUserId);
        return ResponseEntity.ok(dashboardService.getRecentActivity(limit));
    }
}
