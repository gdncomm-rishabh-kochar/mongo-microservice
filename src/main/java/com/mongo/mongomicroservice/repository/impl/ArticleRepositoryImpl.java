package com.mongo.mongomicroservice.repository.impl;

import com.mongo.mongomicroservice.config.SolrProperties;
import com.mongo.mongomicroservice.exception.SolrOperationException;
import com.mongo.mongomicroservice.model.Article;
import com.mongo.mongomicroservice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final SolrClient solrClient;
    private final SolrProperties solrProperties;

    @Override
    public Article save(Article article) {
        if (article.getId() == null || article.getId().isBlank()) {
            article.setId(UUID.randomUUID().toString());
        }
        if (article.getCreatedAt() == null) {
            article.setCreatedAt(Instant.now().toString());
        }
        try {
            solrClient.addBean(solrProperties.getCollection(), article);
            solrClient.commit(solrProperties.getCollection());
            return article;
        } catch (SolrServerException | IOException e) {
            throw new SolrOperationException("Failed to save article with id: " + article.getId(), e);
        }
    }

    @Override
    public Optional<Article> findById(String id) {
        try {
            SolrDocument doc = solrClient.getById(solrProperties.getCollection(), id);
            if (doc == null) {
                return Optional.empty();
            }
            return Optional.of(toArticle(doc));
        } catch (SolrServerException | IOException e) {
            throw new SolrOperationException("Failed to find article with id: " + id, e);
        }
    }

    @Override
    public List<Article> findAll() {
        try {
            SolrQuery query = new SolrQuery("*:*");
            query.setRows(1000);
            QueryResponse response = solrClient.query(solrProperties.getCollection(), query);
            SolrDocumentList docs = response.getResults();
            if (docs == null || docs.isEmpty()) {
                return Collections.emptyList();
            }
            return docs.stream().map(this::toArticle).toList();
        } catch (SolrServerException | IOException e) {
            throw new SolrOperationException("Failed to retrieve articles", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            solrClient.deleteById(solrProperties.getCollection(), id);
            solrClient.commit(solrProperties.getCollection());
        } catch (SolrServerException | IOException e) {
            throw new SolrOperationException("Failed to delete article with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @SuppressWarnings("unchecked")
    private Article toArticle(SolrDocument doc) {
        return Article.builder()
                .id((String) doc.getFieldValue("id"))
                .title((String) doc.getFieldValue("title"))
                .content((String) doc.getFieldValue("content"))
                .author((String) doc.getFieldValue("author"))
                .category((String) doc.getFieldValue("category"))
                .tags((List<String>) (Object) doc.getFieldValues("tags"))
                .createdAt((String) doc.getFieldValue("created_at"))
                .build();
    }
}
