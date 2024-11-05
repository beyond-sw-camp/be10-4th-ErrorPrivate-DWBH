package com.dwbh.backend.dto.counselor_hire;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CounselorDTO {
    @NotBlank
    private String counselorHireTitle;
    @NotBlank
    private String counselorHireContent;
    @NotBlank
    private String counselorHireCounselorGender;
}
