package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class PowerBankNotAvailableException extends BusinessException {

    public PowerBankNotAvailableException(Long powerBankId) {
        super(HttpStatus.CONFLICT, "ALREADY_RENTED", "이미 대여 중이거나 사용할 수 없는 보조배터리입니다. id=" + powerBankId);
    }
}
