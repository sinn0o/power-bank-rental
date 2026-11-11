package com.example.power_bank_rental.rental.dto;

import jakarta.validation.constraints.NotNull;

public record RentalCreateRequest(

        @NotNull(message = "회원 id는 필수입니다.")
        Long memberId,

        @NotNull(message = "보조배터리 id는 필수입니다.")
        Long powerBankId
) {
}
