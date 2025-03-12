package com.ft.library.service;

import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.model.entity.Member;

public interface MemberService {
    void createMember(CreateMemberRequest createMemberRequest);

    Member getMemberById(long id);
}
