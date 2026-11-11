package com.example.power_bank_rental.member.dto;

import com.example.power_bank_rental.member.Member;

import java.time.LocalDateTime;

public record MemberResponse(Long id, String name, String studentNo, LocalDateTime createdAt) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getStudentNo(), member.getCreatedAt());
    }
}
