package com.aarhankhan.scalablechatserver.config;

import com.aarhankhan.scalablechatserver.dto.ChatMessage;
import com.aarhankhan.scalablechatserver.handler.ChatWebSocketHandler;
import com.aarhankhan.scalablechatserver.redis.RedisMessagePublisher;
import com.aarhankhan.scalablechatserver.util.ObjectStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Sinks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.aarhankhan.scalablechatserver.config.ServerConstants.WEBSOCKET_MAPPING;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebSocketConfig {

    @Bean
    public ChatWebSocketHandler webSocketHandler(RedisMessagePublisher redisMessagePublisher, RedisAtomicLong activeUserCounter,
                                                 ObjectStringConverter objectStringConverter) {
        Sinks.Many<ChatMessage> chatMessageSink = Sinks.many().multicast().directBestEffort();
        return new ChatWebSocketHandler(chatMessageSink, redisMessagePublisher, activeUserCounter, objectStringConverter);
    }

    @Bean
    public HandlerMapping webSocketHandlerMapping(ChatWebSocketHandler webSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put(WEBSOCKET_MAPPING, webSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setCorsConfigurations(Collections.singletonMap("*", new CorsConfiguration().applyPermitDefaultValues()));
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
