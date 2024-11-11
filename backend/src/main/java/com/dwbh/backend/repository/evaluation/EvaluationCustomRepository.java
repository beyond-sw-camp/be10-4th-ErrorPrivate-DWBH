package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.Evaluation;

import java.math.BigDecimal;
import java.util.List;

public interface EvaluationCustomRepository {

    Evaluation findByChatSeq(Long chatSeq);

    List<Double> findTemperatureByUserSeq(Long userSeq);
}
