package com.dwbh.backend.repository.evaluation;

import com.dwbh.backend.entity.Evaluation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.dwbh.backend.entity.QChat.chat;
import static com.dwbh.backend.entity.QEvaluation.evaluation;
import static com.dwbh.backend.entity.QNotification.notification;
import static com.dwbh.backend.entity.QUser.user;

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
