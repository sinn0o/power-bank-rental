package com.example.power_bank_rental.powerbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PowerBankCreateRequest(

        @NotBlank(message = "시리얼 번호는 필수입니다.")
        String serialNo,

        @NotNull(message = "용량은 필수입니다.")
        @Positive(message = "용량은 양수여야 합니다.")
        Integer capacityMah,

        @NotBlank(message = "위치는 필수입니다.")
        String location
) {
}
