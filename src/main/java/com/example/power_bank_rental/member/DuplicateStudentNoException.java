package com.example.power_bank_rental.member;

import com.example.power_bank_rental.common.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateStudentNoException extends BusinessException {

    public DuplicateStudentNoException(String studentNo) {
        super(HttpStatus.CONFLICT, "DUPLICATE_STUDENT_NO", "이미 등록된 학번입니다: " + studentNo);
    }
}
