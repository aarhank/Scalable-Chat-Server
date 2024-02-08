package com.aarhankhan.scalablechatserver.redis;

import com.aarhankhan.scalablechatserver.dto.ChatMessage;
import com.aarhankhan.scalablechatserver.handler.ChatWebSocketHandler;
import com.aarhankhan.scalablechatserver.util.ObjectStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.aarhankhan.scalablechatserver.config.ServerConstants.MESSAGE_TOPIC;

@Component
@Slf4j
public class RedisMessageListener {
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectStringConverter objectStringConverter;

    public RedisMessageListener(ReactiveStringRedisTemplate reactiveStringRedisTemplate, ChatWebSocketHandler chatWebSocketHandler, ObjectStringConverter objectStringConverter) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.objectStringConverter = objectStringConverter;
    }

    public Mono<Void> subscribeMessageChannelAndPublishOnWebSocket() {
        return reactiveStringRedisTemplate.listenTo(new PatternTopic(MESSAGE_TOPIC))
                .map(ReactiveSubscription.Message::getMessage)
                .flatMap(message -> objectStringConverter.stringToObject(message, ChatMessage.class))
                .filter(chatMessage -> !chatMessage.getMessage().isEmpty())
                .flatMap(chatWebSocketHandler::sendMessage)
                .then();
    }
}
