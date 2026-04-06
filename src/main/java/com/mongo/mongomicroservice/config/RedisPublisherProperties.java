package com.mongo.mongomicroservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis.publisher")
@Getter
@Setter
public class RedisPublisherProperties {

    private String topic = "publisher-events";
}
