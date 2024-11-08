package com.dwbh.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //웹소켓 핸드셰이크 커넥션을 생성
        log.info("::::::::::: Registering stomp endpoints :::::::::::");

        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        log.info("::::::::::: Configuring message broker :::::::::::");

        config.setApplicationDestinationPrefixes("/pub"); // @MessageMapping으로 라우팅
        config.enableSimpleBroker("/sub"); // 해당 경로를 sub하는 client에게 메세지 전달 (내부 메세지 브로커 활성화)
    }

}