package com.dwbh.backend.dto.counselor_hire;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CounselorDTO {
    @Getter
    private String counselorHireTitle;
    @Getter
    private String counselorHireContent;
    @Getter
    private String counselorHireCounselorGender;
}
