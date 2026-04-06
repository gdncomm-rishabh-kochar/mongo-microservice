package com.mongo.mongomicroservice.controller;

import com.mongo.mongomicroservice.dto.response.RedisPublishResponse;
import com.mongo.mongomicroservice.service.RedisMessagePublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedisMessageController.class)
class RedisMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedisMessagePublisherService redisMessagePublisherService;

    @Test
    void publishReturnsAcceptedResponse() throws Exception {
        when(redisMessagePublisherService.publish(any()))
                .thenReturn(new RedisPublishResponse(
                        "publisher-events",
                        "hello redis",
                        Instant.parse("2026-04-06T00:00:00Z")
                ));

        mockMvc.perform(post("/api/v1/redis/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hello redis\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.topic").value("publisher-events"))
                .andExpect(jsonPath("$.message").value("hello redis"))
                .andExpect(jsonPath("$.publishedAt").exists());
    }
}
