package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.entity.CounselorHire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselorRepository extends JpaRepository<CounselorHire, Long> , CounselorCustomRepository {
    boolean existsByHireSeqAndDelDateIsNotNull(Long hireSeq);
}
