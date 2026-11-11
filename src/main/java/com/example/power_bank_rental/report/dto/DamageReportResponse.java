package com.example.power_bank_rental.report.dto;

import com.example.power_bank_rental.report.DamageReport;

import java.time.LocalDateTime;

public record DamageReportResponse(
        String id,
        Long powerBankId,
        Long memberId,
        String type,
        String description,
        LocalDateTime createdAt
) {

    public static DamageReportResponse from(DamageReport report) {
        return new DamageReportResponse(
                report.getId(),
                report.getPowerBankId(),
                report.getMemberId(),
                report.getType(),
                report.getDescription(),
                report.getCreatedAt()
        );
    }
}
