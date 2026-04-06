package com.mongo.mongomicroservice.service;

import com.mongo.mongomicroservice.dto.request.RedisPublishRequest;
import com.mongo.mongomicroservice.dto.response.RedisPublishResponse;

public interface RedisMessagePublisherService {

    RedisPublishResponse publish(RedisPublishRequest request);
}
