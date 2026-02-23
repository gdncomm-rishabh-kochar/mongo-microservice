package com.mongo.mongomicroservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        String id,
        String name,
        BigDecimal price,
        Integer stock,
        String image,
        Instant createdAt,
        Instant updatedAt
) {
}
