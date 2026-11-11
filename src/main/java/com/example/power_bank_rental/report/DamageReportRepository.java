package com.example.power_bank_rental.report;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DamageReportRepository extends MongoRepository<DamageReport, String> {

    List<DamageReport> findByPowerBankId(Long powerBankId);
}
