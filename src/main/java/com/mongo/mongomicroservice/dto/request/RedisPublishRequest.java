package com.mongo.mongomicroservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RedisPublishRequest {

    @NotBlank(message = "Message must not be blank")
    private String message;
}
