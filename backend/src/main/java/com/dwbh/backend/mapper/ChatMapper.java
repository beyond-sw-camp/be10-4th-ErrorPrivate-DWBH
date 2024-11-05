package com.dwbh.backend.mapper;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.counsel_offer.CounselOfferDTO;
import com.dwbh.backend.dto.user.UserDTO;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.CounselOffer;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.UserRepository;
import com.dwbh.backend.repository.counsel_offer.CounselOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMapper {
    private final CounselOfferRepository counselOfferRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Chat toEntity(ChatDTO.ChatRequestDTO dto) {
        Chat chat = modelMapper.map(dto, Chat.class);

        mapping(chat, dto);
        return chat;
    }

    public ChatDTO toDTO(Chat chat) {
        ChatDTO chatDTO = modelMapper.map(chat, ChatDTO.class);

        CounselOfferDTO counselOfferDTO = modelMapper.map(chat.getCounselOffer(), CounselOfferDTO.class);
        chatDTO.setCounselOfferSeq(counselOfferDTO.getCounselOfferSeq());

        UserDTO sendUserDTO = modelMapper.map(chat.getSendUser(), UserDTO.class);
        chatDTO.setSendSeq(sendUserDTO.getUserSeq());

        UserDTO receiveUserDTO = modelMapper.map(chat.getReceiveUser(), UserDTO.class);
        chatDTO.setReceiveSeq(receiveUserDTO.getUserSeq());

        return chatDTO;
    }

    @AfterMapping
    protected void mapping(@MappingTarget Chat chat, ChatDTO.ChatRequestDTO dto) {
        CounselOffer counselOffer = counselOfferRepository.findById(dto.getCounselOfferSeq()).orElseThrow(()->new CustomException(ErrorCodeType.COUNSEL_OFFER_NOT_FOUND));
        User sendUser = userRepository.findById(dto.getSendSeq()).orElseThrow(()->new CustomException(ErrorCodeType.USER_NOT_FOUND));
        User receiveUser = userRepository.findById(dto.getReceiveSeq()).orElseThrow(()->new CustomException(ErrorCodeType.USER_NOT_FOUND));

        chat.builder(counselOffer, sendUser, receiveUser);
    }
}
