package com.mongo.mongomicroservice.dto.response;

import java.util.List;

public record ArticleResponse(
        String id,
        String title,
        String content,
        String author,
        String category,
        List<String> tags,
        String createdAt
) {
}
