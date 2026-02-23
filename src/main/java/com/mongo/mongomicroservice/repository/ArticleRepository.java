package com.mongo.mongomicroservice.repository;

import com.mongo.mongomicroservice.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Optional<Article> findById(String id);

    List<Article> findAll();

    void deleteById(String id);

    boolean existsById(String id);
}
