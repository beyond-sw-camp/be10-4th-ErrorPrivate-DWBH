package com.dwbh.backend.dto.counselor_hire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CounselorListResponse {
    Page<CounselorDTO> counselorList;
    List<CounselorAgeDTO> counselorAgeList;
    List<CounselorTypeDTO> counselorTypeList;
}
