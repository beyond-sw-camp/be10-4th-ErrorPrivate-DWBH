package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.Evaluation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dwbh.backend.entity.QChat.chat;
import static com.dwbh.backend.entity.evaluation.QEvaluation.evaluation;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Evaluation findByChatSeq(Long chatSeq) {
            return jpaQueryFactory
                    .selectFrom(evaluation)
                    .join(evaluation.chat, chat)
                    .where(chat.chatSeq.eq(chatSeq))
                    .fetchOne();
    }
}
