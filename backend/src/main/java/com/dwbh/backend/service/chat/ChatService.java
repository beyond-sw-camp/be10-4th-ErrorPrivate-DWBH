package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final NotificationService notificationService;

    private final ChatRepository chatRepository;

    private final ModelMapper modelMapper;

    public boolean createChat(ChatDTO.ChatRequestDTO chatRequestDTO) {
        boolean result = false;
        try {
            if (ObjectUtils.isEmpty(chatRequestDTO)) {
                log.error("createChat Error chatRequestDTO : {} ", chatRequestDTO);
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }
            Long chatSeq = chatRepository.save(modelMapper.map(chatRequestDTO, Chat.class)).getChatSeq();

            if(chatSeq == null) {
                throw new CustomException(ErrorCodeType.NOTICE_ERROR);
            }
            notificationService.createNotification(chatSeq);

            result = true;
        } catch (Exception e) {
            if(((CustomException) e).getErrorCode() != null) {
                log.error("createChat Error : {}", ((CustomException) e).getErrorCode().getMessage());
                throw new CustomException(((CustomException) e).getErrorCode());
            } else {
                log.error("createChat Error : {}", e.getMessage());
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }
        }

        return result;
    }

    public List<ChatDTO.ChatResponseDTO> readChatList() {
        List<ChatDTO.ChatResponseDTO> chatResponseDTOList = null;
        try {

             List<Chat> chatList = chatRepository.findAll();

             //TODO 아영 - 이제 어떻게 해서 조인해서 가져올건지?

            chatResponseDTOList = chatList.stream()
                    .map(chat -> modelMapper.map(chat, ChatDTO.ChatResponseDTO.class))
                    .collect(Collectors.toList()); //정렬은 최신순으로 바꾸기

        } catch (Exception e) {
            if(((CustomException) e).getErrorCode() != null) {
                log.error("createChat Error : {}", ((CustomException) e).getErrorCode().getMessage());
                throw new CustomException(((CustomException) e).getErrorCode());
            } else {
                log.error("createChat Error : {}", e.getMessage());
                throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
            }
        }

        return chatResponseDTOList;
    }
}
