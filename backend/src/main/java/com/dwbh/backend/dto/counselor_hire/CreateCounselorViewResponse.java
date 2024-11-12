package com.dwbh.backend.dto.counselor_hire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CreateCounselorViewResponse {
    List<CounselorAgeDTO> counselorAgeList;
    List<CounselorTypeDTO> counselorTypeList;
}
