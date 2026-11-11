package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.rental.dto.RentalCreateRequest;
import com.example.power_bank_rental.rental.dto.RentalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalResponse> rent(@Valid @RequestBody RentalCreateRequest request) {
        RentalResponse response = rentalService.rent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<RentalResponse> returnRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnRental(id));
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> findByMember(@RequestParam Long memberId) {
        return ResponseEntity.ok(rentalService.findByMember(memberId));
    }
}
