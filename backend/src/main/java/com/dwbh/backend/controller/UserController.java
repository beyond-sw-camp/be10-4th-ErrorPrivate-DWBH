package com.dwbh.backend.controller;

import com.dwbh.backend.dto.CreateUserRequest;
import com.dwbh.backend.dto.UserDetailResponse;
import com.dwbh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "User", description = "회원")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/user")
    @Operation(summary = "회원 등록")
    public ResponseEntity<Void> createUser(
            @Validated @RequestBody CreateUserRequest createUserRequest) {

        userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 회원 상세 조회
    @GetMapping("/user/{userSeq}")
    @Operation(summary = "회원 상세 조회")
    public ResponseEntity<UserDetailResponse> getUser(
            @PathVariable Long userSeq) {
        UserDetailResponse response = userService.getUserDetail(userSeq);

        return ResponseEntity.ok(response);
    }
}
