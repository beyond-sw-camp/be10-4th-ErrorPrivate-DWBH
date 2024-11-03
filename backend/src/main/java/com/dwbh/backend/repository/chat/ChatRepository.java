package com.dwbh.backend.repository.chat;

import com.dwbh.backend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatCustomRepository {
}
