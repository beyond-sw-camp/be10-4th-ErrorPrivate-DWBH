package com.dwbh.backend.controller.evaluation;

import com.dwbh.backend.dto.evaluation.EvaluationDTO;
import com.dwbh.backend.dto.evaluation.EvaluationResponse;
import com.dwbh.backend.service.evaluation.EvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "evaluationController")
@RequiredArgsConstructor    // final을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1")
@Slf4j
public class EvaluationController {

    private final EvaluationService evaluationService;

    // 평가 수정을 위한 조회
    @GetMapping("/chat/{id}/evaluation")
    public ResponseEntity<EvaluationResponse> readEvaluation(@PathVariable("id") Long chatId) {

        EvaluationResponse response = evaluationService.readEvaluation(chatId);

        return ResponseEntity.ok(response);
    }
}
