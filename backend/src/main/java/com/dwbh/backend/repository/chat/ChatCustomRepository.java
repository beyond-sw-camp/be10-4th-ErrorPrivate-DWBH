package com.dwbh.backend.repository.chat;

import com.dwbh.backend.dto.chat.ChatDTO;

import java.util.List;

public interface ChatCustomRepository {
    List<ChatDTO.Response> findByUserSeq(Long userSeq);
}
