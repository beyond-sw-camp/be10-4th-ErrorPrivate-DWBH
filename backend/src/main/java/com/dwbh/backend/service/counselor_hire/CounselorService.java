package com.dwbh.backend.service.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorUpdateRequest;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.counselor_hire.*;
import com.dwbh.backend.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    @Transactional
    public void savePost(CounselorResponse savePostReqDTO) {
        Long loginUserId = 1L;  // 예시로 로그인된 사용자 ID 설정

        User foundUser = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));

        // `CounselorAge`와 `CounselorType` 엔티티 조회
//        CounselorAge counselorAge = counselorAgeRepository.findById(savePostReqDTO.getAgeRangeId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 나이대입니다."));
//        CounselorType counselorType = counselorTypeRepository.findById(savePostReqDTO.getTypeId())
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 조건 유형입니다."));

        // `CounselorHire` 엔티티 생성
//        CounselorHire saveCounselor = new CounselorHire(
//                savePostReqDTO.getHireTitle(),
//                savePostReqDTO.getHireContent(),
//                savePostReqDTO.getHireGender(),
//                foundUser,
//                counselorAge,
//                counselorType
//        );
//
//        counselorRepository.save(saveCounselor);
    }

    // 모든 게시글 조회
    public List<CounselorResponse> findAllPosts() {
        List<CounselorResponse> counselorList = counselorRepository.findAllJoinUser();

        for(CounselorResponse counselor : counselorList) {
            counselor.setAgeRanges(counselorRepository.findCounselorAgeByCounselorHireSeq(counselor.getHireSeq()));
            counselor.setTypes(counselorRepository.findCounselorTypeByCounselorHireSeq(counselor.getHireSeq()));
        }

        log.info("counselorList : {}", counselorList);
        return counselorList;
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long id, CounselorUpdateRequest updatePostReqDTO) {
        CounselorHire counselorHire = counselorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        counselorHire.updateCounselor(updatePostReqDTO.getHireTitle(),
                updatePostReqDTO.getHireContent(),
                updatePostReqDTO.getHireGender());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        CounselorHire counselorHire = counselorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        counselorRepository.delete(counselorHire);
    }

    // 게시글 상세조회
    @Transactional
    public CounselorDetailResponse readPostDetail(Long hireSeq, Long currentUserSeq) {
        CounselorHire hire = counselorRepository.findById(hireSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));

        // 삭제된 글 예외처리
        boolean isDeleted = counselorRepository.existsByHireSeqAndDelDateIsNotNull(hireSeq);

        if (isDeleted) {
            throw new CustomException(ErrorCodeType.POST_NOT_FOUND);
        }

        return counselorRepository.findCounselorHireDetail(hireSeq, currentUserSeq);
    }
}
