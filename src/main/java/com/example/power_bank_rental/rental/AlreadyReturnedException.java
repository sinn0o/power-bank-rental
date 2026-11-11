package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyReturnedException extends BusinessException {

    public AlreadyReturnedException(Long rentalId) {
        super(HttpStatus.CONFLICT, "ALREADY_RETURNED", "이미 반납된 대여 내역입니다. id=" + rentalId);
    }
}
