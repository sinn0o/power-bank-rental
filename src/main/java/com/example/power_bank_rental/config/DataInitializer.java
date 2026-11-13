package com.example.power_bank_rental.config;

import com.example.power_bank_rental.member.MemberRepository;
import com.example.power_bank_rental.member.MemberService;
import com.example.power_bank_rental.member.dto.MemberCreateRequest;
import com.example.power_bank_rental.member.dto.MemberResponse;
import com.example.power_bank_rental.powerbank.PowerBankService;
import com.example.power_bank_rental.powerbank.dto.PowerBankCreateRequest;
import com.example.power_bank_rental.powerbank.dto.PowerBankResponse;
import com.example.power_bank_rental.rental.RentalService;
import com.example.power_bank_rental.rental.dto.RentalCreateRequest;
import com.example.power_bank_rental.rental.dto.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// 파일 기반 H2에 예시 데이터(학생 5명, 보조배터리 10개, 대여 이력 일부)를 한 번만 채워 넣는다.
// 이미 데이터가 있으면(재시작) 아무것도 하지 않는다.
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PowerBankService powerBankService;
    private final RentalService rentalService;

    @Override
    public void run(String... args) {
        if (memberRepository.count() > 0) {
            return;
        }

        List<MemberResponse> members = List.of(
                memberService.register(new MemberCreateRequest("김민준", "20231001")),
                memberService.register(new MemberCreateRequest("이서연", "20231002")),
                memberService.register(new MemberCreateRequest("박도윤", "20231003")),
                memberService.register(new MemberCreateRequest("최하은", "20231004")),
                memberService.register(new MemberCreateRequest("정지훈", "20231005"))
        );

        List<PowerBankResponse> powerBanks = List.of(
                powerBankService.register(new PowerBankCreateRequest("PB-001", 10000, "중앙도서관")),
                powerBankService.register(new PowerBankCreateRequest("PB-002", 10000, "중앙도서관")),
                powerBankService.register(new PowerBankCreateRequest("PB-003", 5000, "중앙도서관")),
                powerBankService.register(new PowerBankCreateRequest("PB-004", 10000, "학생회관")),
                powerBankService.register(new PowerBankCreateRequest("PB-005", 10000, "학생회관")),
                powerBankService.register(new PowerBankCreateRequest("PB-006", 5000, "학생회관")),
                powerBankService.register(new PowerBankCreateRequest("PB-007", 10000, "공학관")),
                powerBankService.register(new PowerBankCreateRequest("PB-008", 10000, "공학관")),
                powerBankService.register(new PowerBankCreateRequest("PB-009", 5000, "공학관")),
                powerBankService.register(new PowerBankCreateRequest("PB-010", 10000, "공학관"))
        );

        // 반납 완료된 대여 이력
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(0).id(), powerBanks.get(0).id())));
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(1).id(), powerBanks.get(1).id())));
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(2).id(), powerBanks.get(2).id())));
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(3).id(), powerBanks.get(3).id())));
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(4).id(), powerBanks.get(4).id())));
        returnImmediately(rentalService.rent(new RentalCreateRequest(members.get(0).id(), powerBanks.get(5).id())));

        // 현재 대여 중인 이력 (PB-007, PB-008은 RENTED 상태로 남음)
        rentalService.rent(new RentalCreateRequest(members.get(2).id(), powerBanks.get(6).id()));
        rentalService.rent(new RentalCreateRequest(members.get(4).id(), powerBanks.get(7).id()));
    }

    private void returnImmediately(RentalResponse rental) {
        rentalService.returnRental(rental.id());
    }
}
