package com.dwbh.backend.dto.offer;

import com.dwbh.backend.common.aggregate.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserSummaryDTO {

    private String userNickname;
    private Gender userGender;
    private LocalDate userBirthday;

    // 나중에 연관관계 추가 필요
//    private long userProfileFileSeq;

}
