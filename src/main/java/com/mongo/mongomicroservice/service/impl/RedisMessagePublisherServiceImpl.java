package com.mongo.mongomicroservice.service.impl;

import com.mongo.mongomicroservice.config.RedisPublisherProperties;
import com.mongo.mongomicroservice.dto.request.RedisPublishRequest;
import com.mongo.mongomicroservice.dto.response.RedisPublishResponse;
import com.mongo.mongomicroservice.service.RedisMessagePublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessagePublisherServiceImpl implements RedisMessagePublisherService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisPublisherProperties redisPublisherProperties;

    @Override
    public RedisPublishResponse publish(RedisPublishRequest request) {
        Instant publishedAt = Instant.now();
        stringRedisTemplate.convertAndSend(redisPublisherProperties.getTopic(), request.getMessage());
        log.info("Published Redis message to topic {}: {}", redisPublisherProperties.getTopic(), request.getMessage());
        return new RedisPublishResponse(
                redisPublisherProperties.getTopic(),
                request.getMessage(),
                publishedAt
        );
    }
}
