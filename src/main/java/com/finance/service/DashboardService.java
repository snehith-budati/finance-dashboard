package com.finance.service;

import com.finance.entity.FinancialRecord;
import com.finance.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DashboardService {

    private final RecordRepository recordRepository;

    public DashboardService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public BigDecimal getTotalIncome() {
        return recordRepository.findAll().stream()
                .filter(r -> "INCOME".equals(r.getType()))
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses() {
        return recordRepository.findAll().stream()
                .filter(r -> "EXPENSE".equals(r.getType()))
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getNetBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }

    public Map<String, BigDecimal> getCategoryTotals() {
        Map<String, BigDecimal> totals = new TreeMap<>();

        for (FinancialRecord record : recordRepository.findAll()) {
            BigDecimal signedAmount = "EXPENSE".equals(record.getType())
                    ? record.getAmount().negate()
                    : record.getAmount();
            totals.merge(record.getCategory(), signedAmount, BigDecimal::add);
        }

        return totals;
    }

    public Map<String, BigDecimal> getMonthlyTrends() {
        Map<YearMonth, BigDecimal> monthly = new TreeMap<>();

        for (FinancialRecord record : recordRepository.findAll()) {
            YearMonth month = YearMonth.from(record.getDate());
            BigDecimal signedAmount = "EXPENSE".equals(record.getType())
                    ? record.getAmount().negate()
                    : record.getAmount();
            monthly.merge(month, signedAmount, BigDecimal::add);
        }

        Map<String, BigDecimal> response = new LinkedHashMap<>();
        monthly.forEach((k, v) -> response.put(k.toString(), v));
        return response;
    }

    public List<FinancialRecord> getRecentActivity(int limit) {
        return recordRepository.findAll().stream()
                .sorted((a, b) -> {
                    int dateCompare = b.getDate().compareTo(a.getDate());
                    if (dateCompare != 0) {
                        return dateCompare;
                    }
                    return b.getId().compareTo(a.getId());
                })
                .limit(limit)
                .toList();
    }
}
