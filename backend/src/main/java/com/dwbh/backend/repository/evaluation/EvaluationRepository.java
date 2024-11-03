package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.evaluation.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, EvaluationCustomRepository {

}
