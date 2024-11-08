package com.dwbh.backend.controller.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;
import com.dwbh.backend.dto.counselor_hire.CounselorUpdateRequest;
import com.dwbh.backend.service.counselor_hire.CounselorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/counselor-hire")
@Tag(name = "게시판", description = "게시판 API")
public class CounselorController {

    private final CounselorService counselorService;

    @Operation(summary = "전체 게시글 조회", description = "전체 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CounselorResponse>> getAllPosts() {
        List<CounselorResponse> posts = counselorService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 등록", description = "게시글을 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> savePost(@RequestBody CounselorResponse savePostReqDTO) {
        counselorService.savePost(savePostReqDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 수정", description = "ID로 특정 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody CounselorUpdateRequest updatePostReqDTO) {
        counselorService.updatePost(id, updatePostReqDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제", description = "ID로 특정 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        counselorService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 상세조회", description = "ID로 특정 게시글을 조회합니다.")
    @GetMapping("/hire-post/{id}")
    public ResponseEntity<CounselorDetailResponse> readPostDetail(@PathVariable Long id) {

        CounselorDetailResponse detail = counselorService.readPostDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(detail);
    }

}
