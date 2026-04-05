package com.finance.controller;

import com.finance.entity.FinancialRecord;
import com.finance.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<FinancialRecord>> getAllRecords() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    @PostMapping
    public ResponseEntity<FinancialRecord> addRecord(@RequestBody FinancialRecord record) {
        return ResponseEntity.ok(recordService.addRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecord> updateRecord(@PathVariable Long id, @RequestBody FinancialRecord record) {
        return ResponseEntity.ok(recordService.updateRecord(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
