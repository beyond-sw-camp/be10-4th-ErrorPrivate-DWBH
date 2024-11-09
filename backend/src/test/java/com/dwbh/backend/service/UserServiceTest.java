package com.dwbh.backend.service;

import com.dwbh.backend.dto.CreateUserRequest;
import com.dwbh.backend.dto.user.ModifyUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@DisplayName("User Service 테스트")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("회원 등록 테스트")
    void createUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "test@test.te",
                "testtestaA!",
                "test",
                "test",
                LocalDate.of(1900, 1, 1),
                "test");

        Assertions.assertDoesNotThrow(() -> userService.createUser(createUserRequest, "test@test.te"));
    }

    private static Stream<Arguments> emailCheckParam() {
        return Stream.of(
                arguments(true, "test1@example.com"),
                arguments(true, "test2@example.com"),
                arguments(true, "test3@example.com"),
                arguments(false, "test4@example.com")
        );
    }

    @DisplayName("이메일 중복 확인 테스트")
    @ParameterizedTest(name = "{1} = {0}")
    @MethodSource("emailCheckParam")
    void emailCheck(boolean result, String email) {
        assertEquals(result, userService.emailCheck(email));
    }

    private static Stream<Arguments> nicknameCheckParam() {
        return Stream.of(
                arguments(true, "user1"),
                arguments(true, "user2"),
                arguments(true, "user3"),
                arguments(false, "user4")
        );
    }

    @DisplayName("닉네임 중복 확인 테스트")
    @ParameterizedTest(name = "{1} = {0}")
    @MethodSource("nicknameCheckParam")
    void nicknameCheck(boolean result, String nickname) {
        assertEquals(result, userService.nicknameCheck(nickname));
    }

    private static Stream<Arguments> getUserSeqParam() {
        return Stream.of(
                arguments("1", "test1@example.com"),
                arguments("2", "test2@example.com"),
                arguments("3", "test3@example.com")
        );
    }

    @DisplayName("user_email 로 user_seq 검색 테스트")
    @ParameterizedTest(name = "{1}: userSeq = {0}")
    @MethodSource("getUserSeqParam")
    void getUserSeq(long userSeq, String email) {
        assertEquals(userSeq, userService.getUserSeq(email));
    }

    @Test
    @DisplayName("회원 상세 조회 테스트")
    void getUserDetail() {
        Assertions.assertDoesNotThrow(() -> userService.getUserDetail(1L));
    }

    @Test
    @DisplayName("회원 정보 수정 조회 테스트")
    void getUserModify() {
        Assertions.assertDoesNotThrow(() -> userService.getUserModify(1L));
    }

    @Test
    @Transactional
    @DisplayName("회원 정보 수정 테스트")
    void modifyUser() {
        ModifyUserRequest modifyUserRequest = new ModifyUserRequest(
                "user10",
                "testtestaA!",
                "male",
                LocalDate.of(1900, 1, 1),
                "INTJ");

        userService.modifyUser(1L, null, modifyUserRequest);

        assertEquals(true, userService.nicknameCheck("user10"));
    }

    @Test
    @Transactional
    @DisplayName("회원 탈퇴 테스트")
    void deleteUser() {
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1L));
    }
}