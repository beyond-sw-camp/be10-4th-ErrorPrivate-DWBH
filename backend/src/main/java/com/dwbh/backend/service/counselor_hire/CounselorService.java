package com.dwbh.backend.service.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.*;
import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.entity.*;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.counselor_hire.*;
import com.dwbh.backend.repository.user.UserRepository;
import com.dwbh.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;
    private final CounselorAgeRepository counselorAgeRepository;
    private final CounselorTypeRepository counselorTypeRepository;
    private final CounselorHireAgeRepository counselorHireAgeRepository;
    private final CounselorHireTypeRepository counselorHireTypeRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    // 모든 게시글 조회
    public CounselorListResponse readCounselorList(ReadCounselorListRequest request, Pageable pageable) {
        Page<CounselorDTO> counselorList = counselorRepository.findAllJoinUser(request, pageable);

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

        // 현재 로그인한 사용자와 요청의 사용자 검증
        Long currentUserSeq = null;

        String email = AuthUtil.getAuthUser();
        if (email != null && !"anonymousUser".equals(email)) { // 로그인된 경우에만 userSeq 설정
            currentUserSeq = userService.getUserSeq(email);
        }

        if (isDeleted) {
            throw new CustomException(ErrorCodeType.POST_NOT_FOUND);
        }

        return counselorRepository.findCounselorHireDetail(hireSeq, currentUserSeq);
    }

    // 모든 게시글 조회
    public CounselorUserListResponse readUserCounselorList(Long userSeq, Pageable pageable) {
        Page<CounselorDTO> counselorList = counselorRepository.findAllUserCounselListJoinUser(userSeq, pageable);

        return new CounselorUserListResponse(counselorList);
    }

    public CreateCounselorViewResponse createCounselorRequest() {

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

        return new CreateCounselorViewResponse(
                counselorAgeList,
                counselorTypeList
        );
    }

    @Transactional
    public void createPost(CreateCounselorRequest request) {
        CounselorHire counselorHire = modelMapper.map(request, CounselorHire.class);
        User user = userRepository.findById(request.getUserSeq()).orElseThrow();
        counselorHire.builder(user);
        counselorRepository.save(counselorHire);

        List<CounselorHireAge> counselorHireAges = new ArrayList<>();

        for(int i = 0; i < request.getHireAges().size(); i++) {
            Long counselorAgeRangeSeq = request.getHireAges().get(i);
            CounselorAge counselorAge = counselorAgeRepository.findById(counselorAgeRangeSeq).orElseThrow();
            counselorHireAges.add(CounselorHireAge.builder()
                    .counselorAge(counselorAge)
                    .counselorHire(counselorHire)
                    .build());
        }
        counselorHireAgeRepository.saveAll(counselorHireAges);

        CounselorType counselorType = counselorTypeRepository.findById(request.getHireTypes()).orElseThrow();
        CounselorHireType counselorHireType = CounselorHireType.builder().
                counselorType(counselorType).
                counselorHire(counselorHire).
                build();

        counselorHireTypeRepository.save(counselorHireType);
    }
}
