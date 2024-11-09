package com.dwbh.backend.service.counselor_hire;

import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.dto.counselor_hire.CounselorDTO;
import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.CounselorAge;
import com.dwbh.backend.entity.CounselorType;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.counselor_hire.*;
import com.dwbh.backend.repository.user.UserRepository;
import com.dwbh.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;
    private final CounselorAgeRepository counselorAgeRepository;
    private final CounselorTypeRepository counselorTypeRepository;
    private final CounselorCustomRepositoryImpl customRepositoryimpl;
    private final UserService userService;

    // 게시글 등록
    @Transactional
    public void savePost(CounselorDTO savePostReqDTO) {
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
    public List<CounselorDTO> findAllPosts() {
        List<CounselorHire> counselorHires = counselorRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return counselorHires.stream()
                .map(counselor -> new CounselorDTO(
                        counselor.getHireSeq(),
                        counselor.getHireTitle(),
                        counselor.getHireContent(),
                        counselor.getHireGender().toString(),
//                        counselor.getCounselorAge().getCounselorAgeRangeSeq(),
//                        counselor.getCounselorType().getCounselorTypeSeq(),
                        Optional.ofNullable(counselor.getRegDate())
                                .map(date -> date.format(formatter))
                                .orElse(null)
                ))
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long id, CounselorDTO updatePostReqDTO) {
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
    public CounselorDetailResponse readPostDetail(Long hireSeq) {
        CounselorHire hire = counselorRepository.findById(hireSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));

        // 삭제된 글 예외처리
        boolean isDeleted = counselorRepository.existsByHireSeqAndDelDateIsNotNull(hireSeq);

        // 현재 로그인한 사용자와 요청의 사용자 검증
        Long currentUserSeq = null;

        String email = AuthUtil.getAuthUser();
        if (email != null) { // 로그인된 경우에만 userSeq 설정
            currentUserSeq = userService.getUserSeq(email);
        }

        if (isDeleted) {
            throw new CustomException(ErrorCodeType.POST_NOT_FOUND);
        }

        return customRepositoryimpl.findCounselorHireDetail(hireSeq, currentUserSeq);
    }
}
