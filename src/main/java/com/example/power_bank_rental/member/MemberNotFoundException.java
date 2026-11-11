package com.example.power_bank_rental.member;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "존재하지 않는 회원입니다. id=" + id);
    }
}
