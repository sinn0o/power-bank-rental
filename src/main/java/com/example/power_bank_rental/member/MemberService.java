package com.example.power_bank_rental.member;

import com.example.power_bank_rental.member.dto.MemberCreateRequest;
import com.example.power_bank_rental.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse register(MemberCreateRequest request) {
        if (memberRepository.existsByStudentNo(request.studentNo())) {
            throw new DuplicateStudentNoException(request.studentNo());
        }
        Member member = new Member(request.name(), request.studentNo());
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    // rental 도메인 등에서 회원 존재 여부를 확인할 때 사용
    public Member getOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    // 데모 페이지의 회원 목록 조회용
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
