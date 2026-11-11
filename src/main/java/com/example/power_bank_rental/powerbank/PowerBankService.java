package com.example.power_bank_rental.powerbank;

import com.example.power_bank_rental.powerbank.dto.PowerBankCreateRequest;
import com.example.power_bank_rental.powerbank.dto.PowerBankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PowerBankService {

    private final PowerBankRepository powerBankRepository;

    @Transactional
    public PowerBankResponse register(PowerBankCreateRequest request) {
        if (powerBankRepository.existsBySerialNo(request.serialNo())) {
            throw new DuplicateSerialNoException(request.serialNo());
        }
        PowerBank powerBank = new PowerBank(request.serialNo(), request.capacityMah(), request.location());
        powerBankRepository.save(powerBank);
        return PowerBankResponse.from(powerBank);
    }

    public List<PowerBankResponse> findAll(PowerBankStatus status) {
        List<PowerBank> powerBanks = (status == null)
                ? powerBankRepository.findAll()
                : powerBankRepository.findByStatus(status);
        return powerBanks.stream().map(PowerBankResponse::from).toList();
    }

    // rental, report 도메인 등에서 보조배터리 존재 여부를 확인할 때 사용
    public PowerBank getOrThrow(Long id) {
        return powerBankRepository.findById(id)
                .orElseThrow(() -> new PowerBankNotFoundException(id));
    }
}
