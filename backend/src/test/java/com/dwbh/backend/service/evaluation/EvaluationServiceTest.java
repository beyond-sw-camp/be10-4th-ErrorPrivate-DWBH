package com.dwbh.backend.service.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

@SpringBootTest
@TestPropertySource(properties = {
        "MYSQL_ROOT_PASSWORD=root",
        "MYSQL_HOST=localhost",
        "MYSQL_DATABASE=dwbh",
        "MYSQL_USER=dwbh",
        "MYSQL_PASSWORD=dwbh",
        "MYSQL_PORT=3309",
        "MONGO_HOST=localhost",
        "MONGO_PORT=27017",
        "MONGO_AUTHENTICATION_DATABASE=admin",
        "MONGO_INITDB_ROOT_USERNAME=dwbh",
        "MONGO_INITDB_ROOT_PASSWORD=dwbh",
        "MONGO_DATABASE=dwbh",
})
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
                        6L,
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
                () -> evaluationService.deleteEvaluation(6L)
        );
    }
}