package com.finance.controller;

import com.finance.entity.FinancialRecord;
import com.finance.service.AuthorizationService;
import com.finance.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;
    private final AuthorizationService authorizationService;

    public RecordController(RecordService recordService, AuthorizationService authorizationService) {
        this.recordService = recordService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<List<FinancialRecord>> getAllRecords(@RequestHeader("X-User-Id") Long actorUserId) {
        authorizationService.requireAnalyst(actorUserId);
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FinancialRecord>> filterRecords(
            @RequestHeader("X-User-Id") Long actorUserId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        authorizationService.requireAnalyst(actorUserId);
        return ResponseEntity.ok(recordService.filterRecords(type, category, fromDate, toDate));
    }

    @PostMapping
    public ResponseEntity<FinancialRecord> addRecord(
            @RequestHeader("X-User-Id") Long actorUserId,
            @Valid @RequestBody FinancialRecord record) {
        authorizationService.requireAdmin(actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.addRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecord> updateRecord(
            @RequestHeader("X-User-Id") Long actorUserId,
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecord record) {
        authorizationService.requireAdmin(actorUserId);
        return ResponseEntity.ok(recordService.updateRecord(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@RequestHeader("X-User-Id") Long actorUserId, @PathVariable Long id) {
        authorizationService.requireAdmin(actorUserId);
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
