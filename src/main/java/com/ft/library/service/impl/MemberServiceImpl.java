package com.ft.library.service.impl;

import com.ft.library.exception.MemberAlreadyExistsException;
import com.ft.library.exception.MemberNotFound;
import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.MembershipStatus;
import com.ft.library.repository.MemberRepository;
import com.ft.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void createMember(CreateMemberRequest createMemberRequest) {
        memberRepository.findByEmail(createMemberRequest.getEmail()).ifPresent(member -> {
            throw new MemberAlreadyExistsException("Member Already Exists");
        });

        Member newMember = Member.builder()
                .firstName(createMemberRequest.getFirstName())
                .lastName(createMemberRequest.getLastName())
                .email(createMemberRequest.getEmail())
                .membershipDate(LocalDateTime.now())
                .membershipStatus(MembershipStatus.ACTIVE)
                .build();

        memberRepository.save(newMember);
    }

    @Override
    public Member getMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFound("Member Not Found"));
    }

    @Override
    public void updateMember(long id, CreateMemberRequest createMemberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFound("Member Not Found"));
        member.setFirstName(createMemberRequest.getFirstName());
        member.setLastName(createMemberRequest.getLastName());
        member.setEmail(createMemberRequest.getEmail());

        memberRepository.save(member);
    }
}
