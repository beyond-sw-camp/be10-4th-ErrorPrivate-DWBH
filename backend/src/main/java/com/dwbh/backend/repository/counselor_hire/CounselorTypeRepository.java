package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorTypeDTO;
import com.dwbh.backend.entity.CounselorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounselorTypeRepository extends JpaRepository<CounselorType, Long> {

}
