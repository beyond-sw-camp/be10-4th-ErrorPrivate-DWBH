package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.entity.CounselorHire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends JpaRepository<CounselorHire, Long> {
}
