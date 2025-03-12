package com.ft.library.model.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberRequest {
    private String firstName;
    private String lastName;
    private String email;
}
