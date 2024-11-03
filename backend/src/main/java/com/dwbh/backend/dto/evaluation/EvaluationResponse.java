package com.dwbh.backend.dto.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "평가 응답 데이터")
public class EvaluationResponse {
    private EvaluationDTO evaluation;
}
