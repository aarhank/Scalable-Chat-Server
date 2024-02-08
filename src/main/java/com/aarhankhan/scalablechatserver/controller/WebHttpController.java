package com.aarhankhan.scalablechatserver.controller;

import com.aarhankhan.scalablechatserver.dto.Message;
import com.aarhankhan.scalablechatserver.redis.RedisMessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class WebHttpController {
    private final RedisMessagePublisher redisMessagePublisher;

    public WebHttpController(RedisMessagePublisher redisMessagePublisher) {
        this.redisMessagePublisher = redisMessagePublisher;
    }


    @PostMapping("/message")
    public Mono<ResponseEntity<Message>> postMessage(@RequestBody Message message) {
        return redisMessagePublisher.publishChatMessage(message.getMessage())
                .flatMap(aLong -> Mono.just(ResponseEntity.ok(new Message("Sent Successfully"))));
    }


}
