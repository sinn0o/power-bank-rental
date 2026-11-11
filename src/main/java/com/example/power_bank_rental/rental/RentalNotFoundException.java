package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class RentalNotFoundException extends BusinessException {

    public RentalNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "RENTAL_NOT_FOUND", "존재하지 않는 대여 내역입니다. id=" + id);
    }
}
