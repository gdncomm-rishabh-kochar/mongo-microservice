package com.mongo.mongomicroservice.dto.response;

import java.time.Instant;

public record RedisPublishResponse(
        String topic,
        String message,
        Instant publishedAt
) {
}
