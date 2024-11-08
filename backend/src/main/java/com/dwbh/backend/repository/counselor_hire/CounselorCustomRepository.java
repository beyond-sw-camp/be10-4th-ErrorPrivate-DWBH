package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorAgeDTO;
import com.dwbh.backend.dto.counselor_hire.CounselorResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorTypeDTO;

import java.util.List;

public interface CounselorCustomRepository {

    CounselorDetailResponse findCounselorHireDetail(Long hireSeq, Long currentUserSeq);
    List<CounselorTypeDTO> findCounselorTypeByCounselorHireSeq(Long counselorHireSeq);
    List<CounselorAgeDTO> findCounselorAgeByCounselorHireSeq(Long counselorHireSeq);
    List<CounselorResponse> findAllJoinUser();
}
