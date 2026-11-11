package com.example.power_bank_rental.powerbank.dto;

import com.example.power_bank_rental.powerbank.PowerBank;
import com.example.power_bank_rental.powerbank.PowerBankStatus;

public record PowerBankResponse(Long id, String serialNo, Integer capacityMah, PowerBankStatus status, String location) {

    public static PowerBankResponse from(PowerBank powerBank) {
        return new PowerBankResponse(
                powerBank.getId(),
                powerBank.getSerialNo(),
                powerBank.getCapacityMah(),
                powerBank.getStatus(),
                powerBank.getLocation()
        );
    }
}
