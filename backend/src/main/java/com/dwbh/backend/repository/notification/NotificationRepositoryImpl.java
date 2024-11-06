package com.dwbh.backend.repository.notification;

import com.dwbh.backend.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.dwbh.backend.entity.QNotification.notification;
import static com.dwbh.backend.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notification> findByUserSeq(Long userSeq) {
        return jpaQueryFactory
                .selectFrom(notification)
                .join(notification.user, user)
                .where(user.userSeq.eq(userSeq)
                        .and(notification.regDate.after(LocalDateTime.now().minusDays(14))))
                .fetch();
    }
}
