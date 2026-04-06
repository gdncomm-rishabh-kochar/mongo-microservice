package com.mongo.mongomicroservice.service.impl;

import com.mongo.mongomicroservice.dto.request.ArticleRequest;
import com.mongo.mongomicroservice.dto.response.ArticleResponse;
import com.mongo.mongomicroservice.exception.ResourceNotFoundException;
import com.mongo.mongomicroservice.model.Article;
import com.mongo.mongomicroservice.repository.ArticleRepository;
import com.mongo.mongomicroservice.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "solr", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public ArticleResponse create(ArticleRequest request) {
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .category(request.getCategory())
                .tags(request.getTags())
                .build();
        return toResponse(articleRepository.save(article));
    }

    @Override
    public ArticleResponse findById(String id) {
        return articleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id));
    }

    @Override
    public List<ArticleResponse> findAll() {
        return articleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ArticleResponse update(String id, ArticleRequest request) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id));
        existing.setTitle(request.getTitle());
        existing.setContent(request.getContent());
        existing.setAuthor(request.getAuthor());
        existing.setCategory(request.getCategory());
        existing.setTags(request.getTags());
        return toResponse(articleRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article", id);
        }
        articleRepository.deleteById(id);
    }

    private ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor(),
                article.getCategory(),
                article.getTags(),
                article.getCreatedAt()
        );
    }
}
