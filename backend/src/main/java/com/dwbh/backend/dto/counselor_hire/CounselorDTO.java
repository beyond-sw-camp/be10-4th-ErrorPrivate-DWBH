    package com.dwbh.backend.dto.counselor_hire;

    import jakarta.validation.constraints.NotBlank;
    import lombok.*;

    @Getter
    public class CounselorDTO {

        private String hireTitle;

        private String hireContent;

        private String hireGender;
    }
