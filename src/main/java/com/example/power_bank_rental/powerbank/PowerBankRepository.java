package com.example.power_bank_rental.powerbank;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PowerBankRepository extends JpaRepository<PowerBank, Long> {

    boolean existsBySerialNo(String serialNo);

    List<PowerBank> findByStatus(PowerBankStatus status);
}
