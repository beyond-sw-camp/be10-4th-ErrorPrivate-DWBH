package com.dwbh.backend.repository.chat;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dwbh.backend.entity.QChat.chat;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatDTO.Response> findByUserSeq(Long userSeq) {
        QUser sendUser = new QUser("sendUser");
        QUser receiveUser = new QUser("receiveUser");

        return queryFactory.select(
                Projections.constructor(ChatDTO.Response.class,
                        chat.chatSeq,
                        sendUser.userSeq.as("sendUserSeq"),
                        sendUser.userNickname.as("sendUserNickname"),
                        receiveUser.userSeq.as("receiveUserSeq"),
                        receiveUser.userNickname.as("receiveUserNickname"),
                        chat.modDate,
                        chat.endDate))
                .from(chat)
                .join(chat.sendUser, sendUser)
                .join(chat.receiveUser, receiveUser)
                .where(sendUser.userSeq.eq(userSeq)
                        .or(receiveUser.userSeq.eq(userSeq)))
                .fetch();

    }
}
