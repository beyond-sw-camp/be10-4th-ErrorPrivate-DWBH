package com.dwbh.backend.dto.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor    // 모든 값을 전달받은 생성자 생성
@Schema(description = "평가 요청 데이터")
public class EvaluationRequest {
    @NotNull
    private final Long chatSeq;

    @NotNull
    private final Integer evaluationSatisfaction;

    @NotNull
    private final Integer evaluationCommunication;

    @NotNull
    private final Integer evaluationKindness;
}
