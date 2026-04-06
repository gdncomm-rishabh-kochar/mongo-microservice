package com.mongo.mongomicroservice.controller;

import com.mongo.mongomicroservice.dto.request.RedisPublishRequest;
import com.mongo.mongomicroservice.dto.response.RedisPublishResponse;
import com.mongo.mongomicroservice.service.RedisMessagePublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/redis/messages")
@RequiredArgsConstructor
public class RedisMessageController {

    private final RedisMessagePublisherService redisMessagePublisherService;

    @PostMapping
    public ResponseEntity<RedisPublishResponse> publish(@Valid @RequestBody RedisPublishRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(redisMessagePublisherService.publish(request));
    }
}
