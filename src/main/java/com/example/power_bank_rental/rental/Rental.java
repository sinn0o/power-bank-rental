package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.member.Member;
import com.example.power_bank_rental.powerbank.PowerBank;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "rental")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 전용 기본 생성자
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "power_bank_id", nullable = false)
    private PowerBank powerBank;

    private LocalDateTime rentedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    public Rental(Member member, PowerBank powerBank) {
        this.member = member;
        this.powerBank = powerBank;
        this.rentedAt = LocalDateTime.now();
        this.dueAt = this.rentedAt.plusHours(24); // 대여 기한 = 대여 시각 + 24시간
        this.status = RentalStatus.RENTING;
    }

    public boolean isRenting() {
        return this.status == RentalStatus.RENTING;
    }

    // 반납 처리: 반납 시각을 기록하고, 기한을 넘겼으면 OVERDUE로 상태를 결정
    public void returnRental(LocalDateTime now) {
        this.returnedAt = now;
        this.status = now.isAfter(dueAt) ? RentalStatus.OVERDUE : RentalStatus.RETURNED;
    }
}
