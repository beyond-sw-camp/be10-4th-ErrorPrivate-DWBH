package com.dwbh.backend.dto.evaluation;

import com.dwbh.backend.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Schema(description = "평가 DTO")
public class EvaluationDTO {
    private Long evaluationSeq;
    private Chat chatSeq;
    private Integer evaluationSatisfaction;
    private Integer evaluationCommunication;
    private Integer evaluationKindness;
    private Double evaluationScore;
}


