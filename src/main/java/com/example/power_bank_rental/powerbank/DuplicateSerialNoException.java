package com.example.power_bank_rental.powerbank;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateSerialNoException extends BusinessException {

    public DuplicateSerialNoException(String serialNo) {
        super(HttpStatus.CONFLICT, "DUPLICATE_SERIAL_NO", "이미 등록된 시리얼 번호입니다: " + serialNo);
    }
}
