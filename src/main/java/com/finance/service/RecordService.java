package com.finance.service;

import com.finance.entity.FinancialRecord;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.RecordRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<FinancialRecord> getAllRecords() {
        return recordRepository.findAll(Sort.by(Sort.Direction.DESC, "date", "id"));
    }

    public List<FinancialRecord> filterRecords(String type, String category, LocalDate fromDate, LocalDate toDate) {
        Specification<FinancialRecord> spec = Specification.where(null);

        if (type != null && !type.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type.toUpperCase()));
        }

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }

        if (fromDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), fromDate));
        }

        if (toDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), toDate));
        }

        return recordRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "date", "id"));
    }

    public FinancialRecord addRecord(FinancialRecord record) {
        return recordRepository.save(record);
    }

    public FinancialRecord updateRecord(Long id, FinancialRecord updated) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        record.setAmount(updated.getAmount());
        record.setType(updated.getType());
        record.setCategory(updated.getCategory());
        record.setDate(updated.getDate());
        record.setNotes(updated.getNotes());
        return recordRepository.save(record);
    }

    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Record not found");
        }
        recordRepository.deleteById(id);
    }

    public List<FinancialRecord> getRecentActivity(int limit) {
        return recordRepository.findAll(Sort.by(Sort.Direction.DESC, "date", "id"))
                .stream()
                .limit(limit)
                .toList();
    }
}
