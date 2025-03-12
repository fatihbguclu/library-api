package com.ft.library.service.impl;

import com.ft.library.exception.MemberAlreadyExistsException;
import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.model.entity.Member;
import com.ft.library.repository.MemberRepository;
import com.ft.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .build();

        memberRepository.save(newMember);
    }
}
