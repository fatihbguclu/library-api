package com.ft.library.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.library.exception.MemberAlreadyExistsException;
import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createMember_shouldReturnSuccess() throws Exception {
        // Given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");

        // When
        doNothing().when(memberService).createMember(createMemberRequest);

        // Then
        mockMvc.perform(post("/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void createMember_whenMemberServiceThrowsException_shouldReturnError() throws Exception {
        // Given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("Fatih", "Büyükgüçlü", "fatih@gmail.com");

        // When
        doThrow(new MemberAlreadyExistsException("Member Already Exists")).when(memberService).createMember(any(CreateMemberRequest.class));

        // Then
        mockMvc.perform(post("/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Member Already Exists"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }
}