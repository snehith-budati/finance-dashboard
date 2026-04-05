package com.finance.service;

import com.finance.entity.FinancialRecord;
import com.finance.repository.RecordRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final RecordRepository recordRepository;
    public DashboardService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public BigDecimal getTotalIncome() {
        return recordRepository.findAll().stream()
                .filter(r -> r.getType().equals("INCOME"))
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses() {
        return recordRepository.findAll().stream()
                .filter(r -> r.getType().equals("EXPENSE"))
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getNetBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }
}
