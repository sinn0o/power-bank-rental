package com.example.power_bank_rental.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByStudentNo(String studentNo);
}
