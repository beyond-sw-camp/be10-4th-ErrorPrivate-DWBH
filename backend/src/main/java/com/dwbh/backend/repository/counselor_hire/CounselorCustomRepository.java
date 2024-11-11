package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CounselorCustomRepository {

    CounselorDetailResponse findCounselorHireDetail(Long hireSeq, Long currentUserSeq);
    List<CounselorTypeDTO> findCounselorTypeByCounselorHireSeq(Long counselorHireSeq);
    List<CounselorAgeDTO> findCounselorAgeByCounselorHireSeq(Long counselorHireSeq);
    Page<CounselorDTO> findAllJoinUser(ReadCounselorListRequest request, Pageable pageable);
    Page<CounselorDTO> findAllUserCounselListJoinUser(Long userSeq, Pageable pageable);
}
