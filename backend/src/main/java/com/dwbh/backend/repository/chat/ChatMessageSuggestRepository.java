package com.dwbh.backend.repository.chat;

import com.dwbh.backend.dto.chat.suggest.ChatMessageSuggest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageSuggestRepository extends MongoRepository<ChatMessageSuggest, String> {

    ChatMessageSuggest findByChatRoomSeq(String chatRoomSeq);

}
