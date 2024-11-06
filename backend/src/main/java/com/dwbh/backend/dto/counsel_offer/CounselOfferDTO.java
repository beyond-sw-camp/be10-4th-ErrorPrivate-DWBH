package com.dwbh.backend.dto.counsel_offer;

import com.dwbh.backend.common.entity.YnType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CounselOfferDTO {
    private Long counselOfferSeq;
    private Long counselorHireSeq;
    private Long userSeq;

    private String counselOfferContent;
    private YnType counselOfferPrivateYn;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private LocalDateTime counselOfferDelDate;
}
