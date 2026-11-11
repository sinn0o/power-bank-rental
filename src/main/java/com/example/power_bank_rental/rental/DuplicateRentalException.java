package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateRentalException extends BusinessException {

    public DuplicateRentalException(Long memberId) {
        super(HttpStatus.CONFLICT, "DUPLICATE_RENTAL", "이미 대여 중인 보조배터리가 있습니다. memberId=" + memberId);
    }
}
