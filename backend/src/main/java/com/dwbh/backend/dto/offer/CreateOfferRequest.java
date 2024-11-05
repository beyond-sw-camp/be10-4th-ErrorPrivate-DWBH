package com.dwbh.backend.dto.offer;

import com.dwbh.backend.common.aggregate.YnType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 작성 DTO")
public class CreateOfferRequest {

    private long userSeq;   // 댓글 작성자
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String offerContent;    // 댓글 내용
    @NotBlank(message = "비밀 댓글 여부는 필수입니다.")
    private YnType offerPrivateYn;  // 비밀 댓글 여부


}
