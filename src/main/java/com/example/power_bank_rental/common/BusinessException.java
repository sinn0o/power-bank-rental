package com.example.power_bank_rental.common;

import org.springframework.http.HttpStatus;

// 비즈니스 규칙 위반 시 사용하는 공통 예외 (상태 코드 + 에러 코드 + 메시지를 함께 들고 다님)
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public BusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
