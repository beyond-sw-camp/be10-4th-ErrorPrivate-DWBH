package com.dwbh.backend.mapper;

import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Evaluation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluationMapper {

    private final ModelMapper modelMapper;

    public EvaluationResponse toDTO(Evaluation evaluation) {
        return modelMapper.map(evaluation, EvaluationResponse.class);
    }

    public Evaluation toEntity(EvaluationRequest evaluationRequest, Chat chat, Double evaluationScore) {
        Evaluation evaluation = modelMapper.map(evaluationRequest, Evaluation.class);

        evaluation.builder(chat, evaluationScore);

        return evaluation;
    }
}
