package com.mongo.mongomicroservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ArticleRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "Category must not be blank")
    private String category;

    private List<String> tags;
}
