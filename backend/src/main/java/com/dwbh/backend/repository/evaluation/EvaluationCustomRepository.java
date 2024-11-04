package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.Evaluation;

public interface EvaluationCustomRepository {

    Evaluation findByChatSeq(Long chatSeq);
}
