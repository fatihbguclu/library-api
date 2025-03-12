package com.ft.library.model.entity;

import com.ft.library.model.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "membership_date")
    private LocalDateTime membershipDate;

    @Column(name = "membership_status")
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;
}
