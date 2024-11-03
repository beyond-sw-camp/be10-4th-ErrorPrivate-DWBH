package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.evaluation.Evaluation;

public interface EvaluationCustomRepository {

    Evaluation findByChatSeq(Long chatSeq);
}
