package com.dwbh.backend.service.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
class EvaluationServiceTest {

    @Autowired
    private EvaluationService evaluationService;

    private static Stream<Arguments> createEvaluationArguments() {
        return Stream.of(
                Arguments.of(
                    3L,
                        1,
                        4,
                        5
                )
        );
    }

    private static Stream<Arguments> updateEvaluationArguments() {
        return Stream.of(
                Arguments.of(
                        4L,
                        3L,
                        5,
                        5,
                        5
                )
        );
    }

    /* 평가 조회 테스트 */
    @Test
    void testReadEvaluation() {
        Assertions.assertDoesNotThrow(
                () -> evaluationService.readEvaluation(3L)
        );
    }

    /* 평가 댓글 조회 테스트 */
    @Test
    void testReadEvaluationComment() {
        Assertions.assertDoesNotThrow(
                () -> evaluationService.readEvaluationComment(3L)
        );
    }

    /* 평가 작성 테스트 */
    @ParameterizedTest
    @MethodSource("createEvaluationArguments")
    void testCreateEvaluation(
            Long chatSeq, Integer evaluationSatisfaction, Integer evaluationCommunication, Integer evaluationKindness
    ) {

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                chatSeq,
                evaluationSatisfaction,
                evaluationCommunication,
                evaluationKindness
        );

        Assertions.assertDoesNotThrow(
                () -> evaluationService.createEvaluation(evaluationRequest)
        );
    }

    /* 평가 수정 테스트 */
    @ParameterizedTest
    @MethodSource("updateEvaluationArguments")
    void testUpdateEvaluation(
            Long evaluationSeq,
            Long chatSeq, Integer evaluationSatisfaction, Integer evaluationCommunication, Integer evaluationKindness
    ) {

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                chatSeq,
                evaluationSatisfaction,
                evaluationCommunication,
                evaluationKindness
        );

        Assertions.assertDoesNotThrow(
                () -> evaluationService.updateEvaluation(evaluationSeq, evaluationRequest)
        );
    }

    /* 평가 삭제 테스트 */
    @Test
    void testDeleteEvaluation() {
        Assertions.assertDoesNotThrow(
                () -> evaluationService.deleteEvaluation(4L)
        );
    }

    /* 평가 가능 유저 체킹 테스트 */
    @Test
    void testCheckUser() {
        Assertions.assertDoesNotThrow(
                () -> evaluationService.checkUser(3L, "kkm7hjh@naver.com")
        );
    }
}