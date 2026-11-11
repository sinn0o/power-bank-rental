package com.example.power_bank_rental.rental.dto;

import com.example.power_bank_rental.rental.Rental;
import com.example.power_bank_rental.rental.RentalStatus;

import java.time.LocalDateTime;

public record RentalResponse(
        Long id,
        Long memberId,
        Long powerBankId,
        LocalDateTime rentedAt,
        LocalDateTime dueAt,
        LocalDateTime returnedAt,
        RentalStatus status
) {

    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getMember().getId(),
                rental.getPowerBank().getId(),
                rental.getRentedAt(),
                rental.getDueAt(),
                rental.getReturnedAt(),
                rental.getStatus()
        );
    }
}
