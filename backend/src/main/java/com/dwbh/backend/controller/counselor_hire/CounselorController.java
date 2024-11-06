package com.dwbh.backend.controller.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorDTO;
import com.dwbh.backend.service.counselor_hire.CounselorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/counselor")
@Tag(name = "게시판", description = "게시판 API")
public class CounselorController {

    private final CounselorService counselorService;

    @Operation(summary = "전체 게시글 조회", description = "전체 게시글 목록을 조회합니다.")
    @GetMapping("/hire-posts")
    public ResponseEntity<List<CounselorDTO>> getAllPosts() {
        List<CounselorDTO> posts = counselorService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 등록", description = "게시글을 등록합니다.")
    @PostMapping("/hire-post")
    public ResponseEntity<Void> savePost(@RequestBody CounselorDTO savePostReqDTO) {
        counselorService.savePost(savePostReqDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 수정", description = "ID로 특정 게시글을 수정합니다.")
    @PutMapping("/hire-post/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody CounselorDTO updatePostReqDTO) {
        counselorService.updatePost(id, updatePostReqDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제", description = "ID로 특정 게시글을 삭제합니다.")
    @DeleteMapping("/hire-post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        counselorService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
