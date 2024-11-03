package com.dwbh.backend.service.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationDTO;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.entity.evaluation.Evaluation;
import com.dwbh.backend.repository.evaluation.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ModelMapper modelMapper;

    // 채팅방번호에 해당하는 평가 조회
    public EvaluationResponse readEvaluation(Long chatId) {
        Evaluation evaluation = evaluationRepository.findByChatSeq(chatId);

        // 엔티티를 DTO 로 변환
        EvaluationDTO evaluationDTO = modelMapper.map(evaluation, EvaluationDTO.class);

        return EvaluationResponse.builder() // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .evaluation(evaluationDTO)
                .build();
    }
}
