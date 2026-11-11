package com.example.power_bank_rental.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// MongoDB damage_report 컬렉션 문서. powerBankId/memberId는 다른 테이블과 조인하지 않는 논리적 참조
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "damage_report")
public class DamageReport {

    @Id
    private String id;

    private Long powerBankId;
    private Long memberId;
    private String type;
    private String description;
    private LocalDateTime createdAt;

    public DamageReport(Long powerBankId, Long memberId, String type, String description) {
        this.powerBankId = powerBankId;
        this.memberId = memberId;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}
