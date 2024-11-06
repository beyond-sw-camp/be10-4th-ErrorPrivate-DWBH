package com.dwbh.backend.config;

import com.dwbh.backend.component.ChatSuggestComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class ChatSuggestConfig {
    @Bean
    public RestClient chatSuggestRestclient(@Value("${chatSuggest.url}") String url,
                                            @Value("${chatSuggest.apiKey}") String apikey) {

        log.info("url : {}", url);
        log.info("apikey : {}", apikey);
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("x-api-key", apikey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public ChatSuggestComponent chatSuggest(@Qualifier("chatSuggestRestclient") RestClient chatSuggestRestclient) {
        RestClientAdapter adapter = RestClientAdapter.create(chatSuggestRestclient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        log.info("chatSuggestRestclient : {}", chatSuggestRestclient);
        return factory.createClient(ChatSuggestComponent.class);
    }
}