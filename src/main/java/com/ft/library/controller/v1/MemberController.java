package com.ft.library.controller.v1;

import com.ft.library.model.dto.request.CreateMemberRequest;
import com.ft.library.model.dto.response.ApiResponse;
import com.ft.library.model.entity.Member;
import com.ft.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createMember(@RequestBody CreateMemberRequest createMemberRequest) {
        memberService.createMember(createMemberRequest);
        return ResponseEntity.ok(ApiResponse.of("Success", "Success", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Member>> getMemberById(@PathVariable long id) {
        Member memberById = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.of("Success", "Success", memberById));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateMember(@PathVariable long id, @RequestBody CreateMemberRequest createMemberRequest) {
        memberService.updateMember(id, createMemberRequest);
        return ResponseEntity.ok(ApiResponse.of("Success", "Success", null));
    }

}
