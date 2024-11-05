package com.dwbh.backend.service.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationDTO;
import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Evaluation;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.evaluation.EvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ModelMapper modelMapper;
    private final ChatRepository chatRepository;

    // 채팅방번호에 해당하는 평가 조회
    @Transactional
    public EvaluationResponse readEvaluation(Long chatSeq) {
        Evaluation evaluation = evaluationRepository.findByChatSeq(chatSeq);

        // 엔티티를 DTO 로 변환
        EvaluationDTO evaluationDTO = modelMapper.map(evaluation, EvaluationDTO.class);

        return EvaluationResponse.builder() // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .evaluation(evaluationDTO)
                .build();
    }

    // 평가 생성
    @Transactional
    public void createEvaluation(EvaluationRequest evaluationRequest) {

        // 이미 채팅방에 해당하는 평가가 생성되어있는지 체크
        if (evaluationRepository.findByChatSeq(evaluationRequest.getChatSeq()) != null) {
            throw new CustomException(ErrorCodeType.COMMON_ERROR);
        }

        // EvaluationRequest를 EvaluationDTO로 매핑하고 추가 연산 수행
        EvaluationDTO evaluationDTO = modelMapper.map(evaluationRequest, EvaluationDTO.class);

        // 평가 점수 연산 후 DTO 에 추가
        Double evaluationScore = (double) (
                (evaluationRequest.getEvaluationCommunication()
                        + evaluationRequest.getEvaluationKindness()
                        + evaluationRequest.getEvaluationSatisfaction()) / 3);
        evaluationDTO.setEvaluationScore(evaluationScore);

        // chat 엔티티 받아오고 DTO 에 추가
        Chat chat = chatRepository.findById(evaluationRequest.getChatSeq())
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 없습니다.."));
        evaluationDTO.setChat(chat);

        // Dto to Entity
        Evaluation evaluation = modelMapper.map(evaluationDTO, Evaluation.class);

        // entity 저장
        evaluationRepository.save(evaluation);
    }

    // 평가 수정
    @Transactional
    public void updateEvaluation(Long evaluationSeq, EvaluationRequest evaluationRequest) {
        // 평가가 없는지 체크
        Evaluation evaluation = evaluationRepository.findById(evaluationSeq)
                .orElseThrow(() -> new EntityNotFoundException("평가가 없습니다.."));

        // 평가 점수 연산
        Double evaluationScore = (double) (
                (evaluationRequest.getEvaluationCommunication()
                        + evaluationRequest.getEvaluationKindness()
                        + evaluationRequest.getEvaluationSatisfaction()) / 3);

        // chat 엔티티 받아옴
        Chat chat = chatRepository.findById(evaluationRequest.getChatSeq())
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 없습니다.."));

        evaluation.updateEvaluation(
                chat,
                evaluationRequest.getEvaluationSatisfaction(),
                evaluationRequest.getEvaluationCommunication(),
                evaluationRequest.getEvaluationKindness(),
                evaluationScore
        );

    }

    // 평가 삭제
    public void deleteEvaluation(Long evaluationSeq) {
        // 평가가 없는지 체크
        Evaluation evaluation = evaluationRepository.findById(evaluationSeq)
                .orElseThrow(() -> new EntityNotFoundException("평가가 없습니다.."));

        evaluationRepository.delete(evaluation);
    }
}
