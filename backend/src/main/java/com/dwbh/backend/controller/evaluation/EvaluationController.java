package com.dwbh.backend.controller.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationRequest;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.service.evaluation.EvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "evaluationController")
@RequiredArgsConstructor    // final을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1")
@Slf4j
public class EvaluationController {

    private final EvaluationService evaluationService;

    // 평가 수정을 위한 조회
    @GetMapping("/chat/{chatSeq}/evaluation")
    public ResponseEntity<EvaluationResponse> readEvaluation(@PathVariable("chatSeq") Long chatSeq) {

        EvaluationResponse response = evaluationService.readEvaluation(chatSeq);

        return ResponseEntity.ok(response);
    }

    // 평가 작성
    @PostMapping("/evaluation")
    public ResponseEntity<String> createEvaluation(
            @RequestBody EvaluationRequest evaluationRequest
    ) {
        evaluationService.createEvaluation(evaluationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("평가작성성공");
    }

    // 평가 수정
    @PutMapping("/evaluation/{evaluationSeq}")
    public ResponseEntity<String> updateEvaluation(
            @PathVariable Long evaluationSeq,
            @RequestBody EvaluationRequest evaluationRequest
    ) {
        evaluationService.updateEvaluation(evaluationSeq, evaluationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("평가수정성공");
    }

    // 평가 삭제
    @DeleteMapping("/evaluation/{evaluationSeq}")
    public ResponseEntity<String> deleteEvaluation(
            @PathVariable Long evaluationSeq
    ) {
        evaluationService.deleteEvaluation(evaluationSeq);

        return ResponseEntity.status(HttpStatus.CREATED).body("평가삭제성공");
    }
}
