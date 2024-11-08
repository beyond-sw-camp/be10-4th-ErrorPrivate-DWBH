package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;

public interface CounselorCustomRepository {

    CounselorDetailResponse findCounselorHireDetail(Long hireSeq, Long currentUserSeq);
}
