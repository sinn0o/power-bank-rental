package com.example.power_bank_rental.report;

import com.example.power_bank_rental.powerbank.PowerBankService;
import com.example.power_bank_rental.report.dto.DamageReportCreateRequest;
import com.example.power_bank_rental.report.dto.DamageReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DamageReportService {

    private final DamageReportRepository damageReportRepository;
    private final PowerBankService powerBankService;

    public DamageReportResponse create(Long powerBankId, DamageReportCreateRequest request) {
        powerBankService.getOrThrow(powerBankId); // JPA로 보조배터리 존재 여부를 먼저 확인

        DamageReport report = new DamageReport(powerBankId, request.memberId(), request.type(), request.description());
        damageReportRepository.save(report);
        return DamageReportResponse.from(report);
    }

    public List<DamageReportResponse> findByPowerBank(Long powerBankId) {
        powerBankService.getOrThrow(powerBankId);

        return damageReportRepository.findByPowerBankId(powerBankId)
                .stream()
                .map(DamageReportResponse::from)
                .toList();
    }
}
