package com.example.power_bank_rental.powerbank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "power_bank")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 전용 기본 생성자
public class PowerBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNo;

    private Integer capacityMah;

    @Enumerated(EnumType.STRING)
    private PowerBankStatus status;

    private String location;

    @Version
    private Long version; // 동시에 들어오는 대여 요청 충돌을 막기 위한 낙관적 락

    public PowerBank(String serialNo, Integer capacityMah, String location) {
        this.serialNo = serialNo;
        this.capacityMah = capacityMah;
        this.location = location;
        this.status = PowerBankStatus.AVAILABLE;
    }

    public void rent() {
        this.status = PowerBankStatus.RENTED;
    }

    public void markAvailable() {
        this.status = PowerBankStatus.AVAILABLE;
    }
}
