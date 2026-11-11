package com.example.power_bank_rental.common;

// 모든 에러 응답을 동일한 형식으로 내려주기 위한 DTO
public record ErrorResponse(int status, String code, String message) {

    public static ErrorResponse of(BusinessException e) {
        return new ErrorResponse(e.getStatus().value(), e.getCode(), e.getMessage());
    }
}
