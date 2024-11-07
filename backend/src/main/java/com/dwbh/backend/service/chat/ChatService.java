package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.chat.ChatRoomDTO;
import com.dwbh.backend.dto.chat.ChatSuggestRequest;
import com.dwbh.backend.dto.chat.suggest.ChatMessageSuggest;
import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.mapper.ChatMapper;
import com.dwbh.backend.mapper.NotificationMapper;
import com.dwbh.backend.repository.chat.ChatMessageRepository;
import com.dwbh.backend.repository.chat.ChatMessageSuggestRepository;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.counsel_offer.CounselOfferRepository;
import com.dwbh.backend.repository.counselor_hire.CounselorRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationRepository notificationRepository;
    private final ChatMapper chatMapper;
    private final NotificationMapper notificationMapper;
    private final ModelMapper modelMapper;
    private final ChatMessageSuggestRepository chatMessageSuggestRepository;
    private final CounselOfferRepository counselOfferRepository;

    @Transactional
    public boolean createChat(ChatDTO.Create chatCreateDTO) {
        boolean result = false;
        try {
            if (ObjectUtils.isEmpty(chatCreateDTO)) {
                log.error("createChat Error chatCreateDTO : {} ", chatCreateDTO);
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }

            Chat chat = chatMapper.toEntity(chatCreateDTO);

            chatRepository.save(chat);

            ChatDTO chatDTO = chatMapper.toDTO(chat);

            if(chatDTO.getChatSeq() == null) {
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }

            // 채팅방 생성 완료 시 채팅 프롬프트 생성
            String counselorContent = counselOfferRepository.findCounselorContentByCounselOfferSeq(chatCreateDTO.getCounselOfferSeq()) + " 대답은 항상 30글자 이내 5줄 이내로 작성해주세요.";
            chatMessageSuggestRepository.save(new ChatMessageSuggest(chatDTO.getChatSeq(), new ChatSuggestRequest(counselorContent, "user", new ArrayList<>()).getContents()));

            // 채팅방 생성 완료 시 알림 생성
            Notification notification = notificationMapper.toEntity(new CreateNotificationRequest(chatDTO.getChatSeq(), chatDTO.getReceiveSeq()));

            notificationRepository.save(notification);

            result = true;
        } catch (Exception e) {
            log.error("createChat Error : {}", e.getMessage());
        }

        return result;
    }

    public List<ChatDTO.Response> readChatList() {
        List<ChatDTO.Response> chatResponseList = null;
        try {

             List<Chat> chatList = chatRepository.findAll();

             //TODO 아영 - mongodb에서 last message 가져오기

            chatResponseList = chatList.stream()
                    .map(chat -> modelMapper.map(chat, ChatDTO.Response.class))
                    .collect(Collectors.toList()); //정렬은 최신순으로 바꾸기

        } catch (Exception e) {
            log.error("readChatList Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return chatResponseList;
    }

    public ChatRoomDTO readChatRoom(String roomId) {
        ChatRoomDTO chatResponse = null;
        try {

            //TODO 아영 - mongodb에서 채팅 내용 가져오기
            chatResponse = chatMessageRepository.findRoomById(roomId);

        } catch (Exception e) {
            log.error("readChatRoom Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return chatResponse;
    }

}
