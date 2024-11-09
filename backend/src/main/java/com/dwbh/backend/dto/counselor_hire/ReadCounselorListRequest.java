package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadCounselorListRequest {
    private Long searchAgeSeq;
    private Gender searchGender;
    private Long searchTypeSeq;
    private String searchTitle;
}
