package com.example.power_bank_rental.rental;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // 해당 회원이 현재 RENTING 상태인 대여를 이미 가지고 있는지 확인
    boolean existsByMemberIdAndStatus(Long memberId, RentalStatus status);

    List<Rental> findByMemberIdOrderByRentedAtDesc(Long memberId);
}
