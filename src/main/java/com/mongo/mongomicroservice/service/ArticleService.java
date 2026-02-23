package com.mongo.mongomicroservice.service;

import com.mongo.mongomicroservice.dto.request.ArticleRequest;
import com.mongo.mongomicroservice.dto.response.ArticleResponse;

import java.util.List;

public interface ArticleService {

    ArticleResponse create(ArticleRequest request);

    ArticleResponse findById(String id);

    List<ArticleResponse> findAll();

    ArticleResponse update(String id, ArticleRequest request);

    void delete(String id);
}
