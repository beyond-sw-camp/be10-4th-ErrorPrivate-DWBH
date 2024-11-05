package com.dwbh.backend.controller.offer;

import com.dwbh.backend.dto.offer.CreateOfferRequest;
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
@Tag(name = "Counsel Offer", description = "게시글 댓글")
@Slf4j
public class OfferController {

    private final OfferService offerService;

    // 댓글 등록
    @PostMapping("/{postSeq}/comment")
    @Operation(summary = "게시글 댓글 등록", description = "게시글에 댓글을 등록한다.")
//    public ResponseEntity<String> createOffer(
    public ResponseEntity<OfferDTO> createOffer(
            @PathVariable Long postSeq,
            @Valid @RequestPart CreateOfferRequest createOfferRequest,
            @RequestPart(required = false) MultipartFile file) {
        log.info("POST /api/v1/hire-post/{postSeq}/comment 댓글 등록 요청 - {}, {}, {}", postSeq, createOfferRequest, file);

//        offerService.createOffer(postSeq, createOfferRequest, file);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("댓글 작성 완료");
        OfferDTO response = offerService.createOffer(postSeq, createOfferRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
