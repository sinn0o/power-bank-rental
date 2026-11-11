package com.example.power_bank_rental.rental;

import com.example.power_bank_rental.member.Member;
import com.example.power_bank_rental.member.MemberService;
import com.example.power_bank_rental.powerbank.PowerBank;
import com.example.power_bank_rental.powerbank.PowerBankService;
import com.example.power_bank_rental.powerbank.PowerBankStatus;
import com.example.power_bank_rental.rental.dto.RentalCreateRequest;
import com.example.power_bank_rental.rental.dto.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MemberService memberService;
    private final PowerBankService powerBankService;

    @Transactional
    public RentalResponse rent(RentalCreateRequest request) {
        Member member = memberService.getOrThrow(request.memberId());
        PowerBank powerBank = powerBankService.getOrThrow(request.powerBankId());

        // AVAILABLE 상태의 배터리만 대여 가능
        if (powerBank.getStatus() != PowerBankStatus.AVAILABLE) {
            throw new PowerBankNotAvailableException(powerBank.getId());
        }
        // 한 회원은 RENTING 상태 대여를 동시에 1개만 가질 수 있음
        if (rentalRepository.existsByMemberIdAndStatus(member.getId(), RentalStatus.RENTING)) {
            throw new DuplicateRentalException(member.getId());
        }

        powerBank.rent();
        Rental rental = new Rental(member, powerBank);
        rentalRepository.save(rental);
        return RentalResponse.from(rental);
    }

    @Transactional
    public RentalResponse returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if (!rental.isRenting()) {
            throw new AlreadyReturnedException(rentalId);
        }

        rental.returnRental(LocalDateTime.now());
        rental.getPowerBank().markAvailable();
        return RentalResponse.from(rental);
    }

    public List<RentalResponse> findByMember(Long memberId) {
        memberService.getOrThrow(memberId); // 존재하지 않는 회원이면 404
        return rentalRepository.findByMemberIdOrderByRentedAtDesc(memberId)
                .stream()
                .map(RentalResponse::from)
                .toList();
    }
}
