package com.finance.service;

import com.finance.entity.FinancialRecord;
import com.finance.repository.RecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<FinancialRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    public FinancialRecord addRecord(FinancialRecord record) {
        return recordRepository.save(record);
    }

    public FinancialRecord updateRecord(Long id, FinancialRecord updated) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        record.setAmount(updated.getAmount());
        record.setType(updated.getType());
        record.setCategory(updated.getCategory());
        record.setDate(updated.getDate());
        record.setNotes(updated.getNotes());
        return recordRepository.save(record);
    }

    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
