package com.example.power_bank_rental.powerbank;

import com.example.power_bank_rental.powerbank.dto.PowerBankCreateRequest;
import com.example.power_bank_rental.powerbank.dto.PowerBankResponse;
import com.example.power_bank_rental.report.DamageReportService;
import com.example.power_bank_rental.report.dto.DamageReportCreateRequest;
import com.example.power_bank_rental.report.dto.DamageReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/power-banks")
@RequiredArgsConstructor
public class PowerBankController {

    private final PowerBankService powerBankService;
    private final DamageReportService damageReportService;

    @PostMapping
    public ResponseEntity<PowerBankResponse> register(@Valid @RequestBody PowerBankCreateRequest request) {
        PowerBankResponse response = powerBankService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PowerBankResponse>> findAll(@RequestParam(required = false) PowerBankStatus status) {
        return ResponseEntity.ok(powerBankService.findAll(status));
    }

    // 고장 신고 등록 (MongoDB)
    @PostMapping("/{id}/reports")
    public ResponseEntity<DamageReportResponse> createReport(
            @PathVariable Long id,
            @Valid @RequestBody DamageReportCreateRequest request
    ) {
        DamageReportResponse response = damageReportService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 배터리별 고장 신고 목록 조회 (MongoDB)
    @GetMapping("/{id}/reports")
    public ResponseEntity<List<DamageReportResponse>> findReports(@PathVariable Long id) {
        return ResponseEntity.ok(damageReportService.findByPowerBank(id));
    }
}
