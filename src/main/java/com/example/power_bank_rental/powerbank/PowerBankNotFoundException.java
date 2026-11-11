package com.example.power_bank_rental.powerbank;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class PowerBankNotFoundException extends BusinessException {

    public PowerBankNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "POWER_BANK_NOT_FOUND", "존재하지 않는 보조배터리입니다. id=" + id);
    }
}
