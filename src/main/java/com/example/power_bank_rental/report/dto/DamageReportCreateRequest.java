package com.example.power_bank_rental.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DamageReportCreateRequest(

        @NotNull(message = "신고자 회원 id는 필수입니다.")
        Long memberId,

        @NotBlank(message = "고장 유형은 필수입니다.")
        String type,

        @NotBlank(message = "상세 설명은 필수입니다.")
        String description
) {
}
