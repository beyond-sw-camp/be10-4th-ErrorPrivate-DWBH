package com.dwbh.backend.mapper;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.NotificationDTO;
import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.dto.user.UserDTO;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.UserRepository;
import com.dwbh.backend.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationMapper {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Notification toEntity(CreateNotificationRequest request) {
        Notification notification = modelMapper.map(request, Notification.class);

        mapping(notification, request);
        return notification;
    }

    @AfterMapping
    protected void mapping(@MappingTarget Notification notification, CreateNotificationRequest request) {
        Chat chat = chatRepository.findById(request.getChatSeq()).orElseThrow(()->new CustomException(ErrorCodeType.CHAT_NOT_FOUND));
        User user = userRepository.findById(request.getUserSeq()).orElseThrow(()->new CustomException(ErrorCodeType.USER_NOT_FOUND));

        notification.builder(chat, user);
    }

    public NotificationDTO toDTO(Notification notification) {
        NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);

        UserDTO userDTO = modelMapper.map(notification.getUser(), UserDTO.class);
        notificationDTO.setUserSeq(userDTO.getUserSeq());

        ChatDTO chatDTO = modelMapper.map(notification.getChat(), ChatDTO.class);
        notificationDTO.setChatSeq(chatDTO.getChatSeq());
        return notificationDTO;
    }
}
