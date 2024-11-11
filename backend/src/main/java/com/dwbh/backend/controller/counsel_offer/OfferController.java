package com.dwbh.backend.controller.counsel_offer;

import com.dwbh.backend.dto.counsel_offer.CreateOrUpdateOfferRequest;
import com.dwbh.backend.dto.counsel_offer.OfferResponse;
import com.dwbh.backend.service.counsel_offer.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hire-post/{hireSeq}/comment")
@Tag(name = "Counsel Offer API", description = "게시글 댓글 API")
@Slf4j
public class OfferController {

    private final OfferService offerService;

    /* 댓글 등록 */
    @PostMapping
    @Operation(summary = "게시글 댓글 등록", description = "게시글에 댓글을 등록한다.")
    public ResponseEntity<OfferResponse> createOffer(
            @PathVariable Long hireSeq,
            @Valid @RequestPart CreateOrUpdateOfferRequest request,
            @RequestPart(required = false) MultipartFile file) {
        log.info("POST /api/v1/hire-post/{}/comment 댓글 등록 요청 - request:{}, file: {}", hireSeq, request, file);

        OfferResponse response = offerService.createOffer(hireSeq, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* 댓글 수정 */
    @PutMapping("/{offerSeq}")
    @Operation(summary = "게시글 댓글 수정", description = "게시글의 특정 댓글을 수정한다.")
    public ResponseEntity<OfferResponse> updateOffer(
            @PathVariable Long hireSeq,
            @PathVariable Long offerSeq,
            @Valid @RequestPart CreateOrUpdateOfferRequest request,
            @RequestPart(required = false) MultipartFile file) {
        log.info("PUT /api/v1/hire-post/{}/comment/{} 댓글 수정 요청 -  request: {}, file: {}", hireSeq, offerSeq, request, file);

        OfferResponse response = offerService.updateOffer(hireSeq, offerSeq, request, file);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/{offerSeq}")
    @Operation(summary = "게시글 댓글 삭제", description = "게시글의 특정 댓글을 삭제한다.")
    public ResponseEntity<Void> deleteOffer( @PathVariable Long hireSeq,
                                             @PathVariable Long offerSeq) {
        log.info("DELETE /api/v1/hire-post/{}/comment/{} 댓글 삭제 요청", hireSeq, offerSeq);

        offerService.deleteOffer(offerSeq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /* 댓글 조회 */
    @GetMapping
    @Operation(summary = "게시글 댓글 조회", description = "특정 게시글의 댓글을 조회한다.")
    public ResponseEntity<Page<OfferResponse>> readOffer(
            @PathVariable Long hireSeq,
            @RequestParam Long currentUserSeq,  // 현재 로그인한 사용자
            @RequestParam(defaultValue = "asc") String sortOrder,  // 기본값: 최신순(desc)
            @PageableDefault(size = 10) Pageable pageable) {

        log.info("GET /api/v1/hire-post/{}/comment 댓글 조회 요청 - sortOrder: {}, pageable: {}", hireSeq, sortOrder, pageable);


        Page<OfferResponse> offerList = offerService.readOffersByHireSeq(hireSeq, currentUserSeq, pageable, sortOrder);

        return ResponseEntity.status(HttpStatus.OK).body(offerList);
    }

    /* 댓글 조회 */
    @GetMapping("/user/comment/{userSeq}")
    @Operation(summary = "유저가 작성한 게시글 댓글 조회", description = "해당 유저가 작성한 댓글을 조회한다.")
    public ResponseEntity<Page<OfferResponse>> readUserOffer(@PathVariable Long userSeq, Pageable pageable) {

        Page<OfferResponse> offerList = offerService.readOffersByUserSeq(userSeq, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(offerList);
    }
}
