package com.dwbh.backend.dto.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "평가 응답 데이터")
public class EvaluationResponse {
    private Long evaluationSeq;
    private Integer evaluationSatisfaction;
    private Integer evaluationCommunication;
    private Integer evaluationKindness;
    private Double evaluationScore;
    private Boolean isEvaluation = false;
}
