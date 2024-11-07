package com.dwbh.backend.controller.offer;

import com.dwbh.backend.dto.offer.CreateOrUpdateOfferRequest;
import com.dwbh.backend.dto.offer.OfferDTO;
import com.dwbh.backend.service.offer.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hire-post")
@Tag(name = "Counsel Offer API", description = "게시글 댓글 API")
@Slf4j
public class OfferController {

    private final OfferService offerService;

    /* 댓글 등록 */
    @PostMapping("/{hireSeq}/comment")
    @Operation(summary = "게시글 댓글 등록", description = "게시글에 댓글을 등록한다.")
    public ResponseEntity<OfferDTO> createOffer(
            @PathVariable Long hireSeq,
            @Valid @RequestPart CreateOrUpdateOfferRequest request,
            @RequestPart(required = false) MultipartFile file) {
        log.info("POST /api/v1/hire-post/{hireSeq}/comment 댓글 등록 요청 - {}, {}, {}", hireSeq, request, file);

        OfferDTO response = offerService.createOffer(hireSeq, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* 댓글 수정 */
    @PutMapping("/{hireSeq}/comment/{offerSeq}")
    @Operation(summary = "게시글 댓글 수정", description = "게시글의 특정 댓글을 수정한다.")
    public ResponseEntity<OfferDTO> updateOffer(
            @PathVariable Long hireSeq,
            @PathVariable Long offerSeq,
            @Valid @RequestPart CreateOrUpdateOfferRequest request,
            @RequestPart(required = false) MultipartFile file) {
        log.info("PUT /api/v1/hire-post/{hireSeq}/comment/{offerSeq} 댓글 수정 요청 - {}, {}, {}, {}", hireSeq, offerSeq, request, file);

        OfferDTO response = offerService.updateOffer(hireSeq, offerSeq, request, file);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/{hireSeq}/comment/{offerSeq}")
    @Operation(summary = "게시글 댓글 삭제", description = "게시글의 특정 댓글을 삭제한다.")
    public ResponseEntity<Void> deleteOffer( @PathVariable Long hireSeq,
                                             @PathVariable Long offerSeq) {
        log.info("DELETE /api/v1/hire-post/{hireSeq}/comment/{offerSeq} 댓글 삭제 요청 - {}, {}", hireSeq, offerSeq);

        offerService.deleteOffer(offerSeq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
