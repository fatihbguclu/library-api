package com.ft.library.service;

import com.ft.library.model.dto.request.CreateMemberRequest;

public interface MemberService {
    void createMember(CreateMemberRequest createMemberRequest);
}
