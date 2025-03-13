package com.ft.library.repository;

import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.MembershipStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void saveMember_shouldPersistMember() {
        // Given
        Member member = Member.builder()
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .membershipDate(LocalDateTime.now())
                .build();

        // When
        Member savedMember = memberRepository.save(member);

        // Then
        assertNotNull(savedMember.getId());
        assertEquals(member.getFirstName(), savedMember.getFirstName());
        assertEquals(member.getLastName(), savedMember.getLastName());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getMembershipStatus(), savedMember.getMembershipStatus());
        assertEquals(member.getMembershipDate(), savedMember.getMembershipDate());
    }

    @Test
    void findMemberByEmail_shouldReturnMember() {
        // Given
        Member member = Member.builder()
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .membershipDate(LocalDateTime.now())
                .build();
        testEntityManager.persist(member);

        // When
        Optional<Member> found = memberRepository.findByEmail("fatih@gmail.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals(member.getFirstName(), found.get().getFirstName());
        assertEquals(member.getLastName(), found.get().getLastName());
        assertEquals(member.getEmail(), found.get().getEmail());
        assertEquals(member.getMembershipStatus(), found.get().getMembershipStatus());
        assertEquals(member.getMembershipDate(), found.get().getMembershipDate());
    }

    @Test
    void findMemberByEmail_whenMemberDoesNotExist_shouldReturnEmpty() {
        // Given
        String nonExistentEmail = "nonexistent@gmail.com";

        // When
        Optional<Member> found = memberRepository.findByEmail(nonExistentEmail);

        // Then
        assertTrue(found.isEmpty());
    }

    @Test
    void findById_whenIdExists_shouldReturnMember() {
        // Given
        Member member = Member.builder()
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .membershipDate(LocalDateTime.now())
                .build();
        testEntityManager.persist(member);

        // When
        Optional<Member> found = memberRepository.findById(member.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(member.getFirstName(), found.get().getFirstName());
        assertEquals(member.getLastName(), found.get().getLastName());
        assertEquals(member.getEmail(), found.get().getEmail());
        assertEquals(member.getMembershipStatus(), found.get().getMembershipStatus());
        assertEquals(member.getMembershipDate(), found.get().getMembershipDate());
    }

    @Test
    void findById_whenIdDoesNotExist_shouldReturnEmptyOptional() {
        // Given
        long nonExistentId = 999L;

        // When
        Optional<Member> found = memberRepository.findById(nonExistentId);

        // Then
        assertTrue(found.isEmpty());
    }
}