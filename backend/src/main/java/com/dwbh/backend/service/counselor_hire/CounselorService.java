package com.dwbh.backend.service.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.*;
import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.entity.CounselorAge;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.CounselorType;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.counselor_hire.*;
import com.dwbh.backend.repository.user.UserRepository;
import com.dwbh.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;
    private final CounselorAgeRepository counselorAgeRepository;
    private final CounselorTypeRepository counselorTypeRepository;
    private final UserService userService;

    // 게시글 등록
    @Transactional
    public void savePost(CreateCounselorRequest request) {
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
    public CounselorListResponse readCounselorList(ReadCounselorListRequest request, Pageable pageable) {
        Page<CounselorDTO> counselorList = counselorRepository.findAllJoinUser(request, pageable);

        for(CounselorDTO counselor : counselorList) {
            counselor.setAgeRanges(counselorRepository.findCounselorAgeByCounselorHireSeq(counselor.getHireSeq()));
            counselor.setTypes(counselorRepository.findCounselorTypeByCounselorHireSeq(counselor.getHireSeq()));
        }

        List<CounselorAge> counselorAges = counselorAgeRepository.findAll();
        List<CounselorAgeDTO> counselorAgeList = counselorAges.stream().map(
                counselorAge -> new CounselorAgeDTO(
                        counselorAge.getCounselorAgeRangeSeq(),
                        counselorAge.getCounselorAgeRange()
                )
        ).toList();

        List<CounselorType> counselorTypes = counselorTypeRepository.findAll();
        List<CounselorTypeDTO> counselorTypeList = counselorTypes.stream().map(
                counselorType -> new CounselorTypeDTO(
                        counselorType.getCounselorTypeSeq(),
                        counselorType.getCounselorType()
                )
        ).toList();

        return new CounselorListResponse(
                counselorList,
                counselorAgeList,
                counselorTypeList
        );
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
    public CounselorDetailResponse readPostDetail(Long hireSeq) {
        CounselorHire hire = counselorRepository.findById(hireSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));

        // 삭제된 글 예외처리
        boolean isDeleted = counselorRepository.existsByHireSeqAndDelDateIsNotNull(hireSeq);

        // 현재 로그인한 사용자의 userSeq 가져오기
        Long currentUserSeq = userService.getUserSeq(AuthUtil.getAuthUser());

        // 현재 로그인한 사용자와 요청의 사용자 검증
        if (!currentUserSeq.equals(hire.getUser().getUserSeq())) {
            throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR);
        }

        if (isDeleted) {
            throw new CustomException(ErrorCodeType.POST_NOT_FOUND);
        }

        return counselorRepository.findCounselorHireDetail(hireSeq, currentUserSeq);
    }
}
