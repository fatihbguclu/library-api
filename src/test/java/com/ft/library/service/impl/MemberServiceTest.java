package com.ft.library.service.impl;

import com.ft.library.exception.MemberAlreadyExistsException;
import com.ft.library.exception.MemberNotFoundException;
import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.MembershipStatus;
import com.ft.library.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void createMember_shouldCreateMember() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");
        when(memberRepository.findByEmail(createMemberRequest.getEmail())).thenReturn(Optional.empty());

        // when
        memberService.createMember(createMemberRequest);

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());

        Member member = memberCaptor.getValue();
        assertEquals(createMemberRequest.getFirstName(), member.getFirstName());
        assertEquals(createMemberRequest.getLastName(), member.getLastName());
        assertEquals(createMemberRequest.getEmail(), member.getEmail());
        assertEquals(MembershipStatus.ACTIVE, member.getMembershipStatus());
        assertNotNull(member.getMembershipDate());
    }

    @Test
    void createMember_whenMemberAlreadyExists_shouldThrowException() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");
        Member existingMember = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .email("fatih@gmail.com").build();
        when(memberRepository.findByEmail(createMemberRequest.getEmail())).thenReturn(Optional.of(existingMember));

        // when
        MemberAlreadyExistsException exception = assertThrows(MemberAlreadyExistsException.class, () -> memberService.createMember(createMemberRequest));

        // then
        assertEquals("Member Already Exists", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void getMemberById_shouldReturnMember() {
        // given
        long memberId = 1L;
        Member expectedMember = Member.builder()
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE).build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(expectedMember));

        // when
        Member actualMember = memberService.getMemberById(memberId);

        // then
        assertNotNull(actualMember);
        assertEquals(expectedMember.getId(), actualMember.getId());
        assertEquals(expectedMember.getFirstName(), actualMember.getFirstName());
        assertEquals(expectedMember.getLastName(), actualMember.getLastName());
        assertEquals(expectedMember.getEmail(), actualMember.getEmail());
        assertEquals(expectedMember.getMembershipStatus(), actualMember.getMembershipStatus());

        verify(memberRepository).findById(memberId);
    }

    @Test
    void getMemberById_whenMemberDoesNotExist_shouldThrowException() {
        // given
        long memberId = 999L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(memberId));

        // then
        assertEquals("Member Not Found", exception.getMessage());
        verify(memberRepository).findById(memberId);
    }

    @Test
    void updateMember_shouldUpdateMember() {
        // given
        long memberId = 1L;
        Member existingMember = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE).build();
        CreateMemberRequest updateRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));

        // When
        memberService.updateMember(memberId, updateRequest);

        // Then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());

        Member updatedMember = memberCaptor.getValue();
        assertEquals(updateRequest.getFirstName(), updatedMember.getFirstName());
        assertEquals(updateRequest.getLastName(), updatedMember.getLastName());
        assertEquals(updateRequest.getEmail(), updatedMember.getEmail());
        assertEquals(MembershipStatus.ACTIVE, updatedMember.getMembershipStatus());
    }

    @Test
    void updateMember_whenMemberDoesNotExist_shouldThrowException() {
        // Given
        long nonExistentMemberId = 999L;
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");

        when(memberRepository.findById(nonExistentMemberId)).thenReturn(Optional.empty());

        // When & Then
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(nonExistentMemberId, createMemberRequest));

        assertEquals("Member Not Found", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

}
