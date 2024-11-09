package com.dwbh.backend.service.evaluation;

import com.dwbh.backend.dto.UserDetailResponse;
import com.dwbh.backend.dto.evaluation.EvaluationCommentResponse;
import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Evaluation;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.mapper.EvaluationMapper;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.counsel_offer.CounselOfferRepository;
import com.dwbh.backend.repository.evaluation.EvaluationRepository;
import com.dwbh.backend.repository.user.CustomUserRepository;
import com.dwbh.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ChatRepository chatRepository;
    private final CustomUserRepository customUserRepository;
    private final CounselOfferRepository counselOfferRepository;
    private final EvaluationMapper evaluationMapper;
    private final UserRepository userRepository;

    // 채팅방번호에 해당하는 평가 조회
    @Transactional
    public EvaluationResponse readEvaluation(Long chatSeq) {
        Evaluation evaluation = evaluationRepository.findByChatSeq(chatSeq);

//         채팅방에 해당하는 평가가 있는지 체크
        if (evaluation == null) {
            throw new CustomException(ErrorCodeType.EVALUATION_NOT_FOUND);
        }

        // 엔티티를 DTO 로 변환
        EvaluationResponse evaluationResponse = evaluationMapper.toDTO(evaluation);
        evaluationResponse.setIsEvaluation(true);

        return evaluationResponse;
    }

    // 평가를 하고자 하는 댓글 조회
    @Transactional
    public EvaluationCommentResponse readEvaluationComment(Long chatSeq) {
        // 채팅창 찾기
        Chat chat = chatRepository.findById(chatSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        // 나이대 계산
        LocalDateTime currentDate = LocalDateTime.now();
        Period period = Period.between(chat.getSendUser().getUserBirthday(), currentDate.toLocalDate());
        int userAge = period.getYears() / 10 * 10;

        // 프로필 찾기
        UserDetailResponse userDetailResponse = customUserRepository.findUserDetailResponse(chat.getSendUser().getUserSeq());
        String userProfilePath = userDetailResponse.getFilePath();

        // 엔티티를 DTO 로 변환
        EvaluationCommentResponse evaluationCommentResponse = evaluationMapper.toDTO(chat, userAge, userProfilePath);

        return evaluationCommentResponse;
    }


    // 평가 생성
    @Transactional
    public void createEvaluation(EvaluationRequest evaluationRequest) {

        // 이미 채팅방에 해당하는 평가가 생성되어있는지 체크
        if (evaluationRepository.findByChatSeq(evaluationRequest.getChatSeq()) != null) {
            throw new CustomException(ErrorCodeType.EVALUATION_EXIST_ERROR);
        }

        // 평가 점수 연산
        Double evaluationScore = (double) (
                (evaluationRequest.getEvaluationCommunication()
                        + evaluationRequest.getEvaluationKindness()
                        + evaluationRequest.getEvaluationSatisfaction()) / 3);

        // chat 엔티티 받아옴
        Chat chat = chatRepository.findById(evaluationRequest.getChatSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        // Dto to Entity
        Evaluation evaluation = evaluationMapper.toEntity(evaluationRequest, chat, evaluationScore);

        // entity 저장
        evaluationRepository.save(evaluation);
    }

    // 평가 수정
    @Transactional
    public void updateEvaluation(Long evaluationSeq, EvaluationRequest evaluationRequest) {
        // 평가가 없는지 체크
        Evaluation evaluation = evaluationRepository.findById(evaluationSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.EVALUATION_NOT_FOUND));

        // 평가 점수 연산
        Double evaluationScore = (double) (
                (evaluationRequest.getEvaluationCommunication()
                        + evaluationRequest.getEvaluationKindness()
                        + evaluationRequest.getEvaluationSatisfaction()) / 3);

        // chat 엔티티 받아옴
        Chat chat = chatRepository.findById(evaluationRequest.getChatSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(ErrorCodeType.EVALUATION_NOT_FOUND));

        evaluationRepository.delete(evaluation);
    }

    // 이메일로 유저가 평가를 할 수있는 유저인지 체킹
    public void checkUser(Long chatSeq, String email) {
        // 이메일로 유저 체크
        User user = userRepository.findByUserEmail(email).orElseThrow();

        // chat 엔티티 받아옴
        Chat chat = chatRepository.findById(chatSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        // 게시글 작성자와 로그인한 사용자가 같으면 통과
        if(!chat.getReceiveUser().getUserSeq().equals(user.getUserSeq())) {
            throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR);
        }
    }
}
