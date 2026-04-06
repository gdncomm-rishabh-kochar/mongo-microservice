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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(prefix = "solr", name = "enabled", havingValue = "true", matchIfMissing = true)
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

    /** Safely get a single string from a Solr field (may be stored as single value or multi-valued list). */
    private static String getStringField(SolrDocument doc, String name) {
        Object val = doc.getFieldValue(name);
        if (val == null) return null;
        if (val instanceof Collection<?> col) {
            return col.isEmpty() ? null : String.valueOf(col.iterator().next());
        }
        return String.valueOf(val);
    }

    /** Safely get a list of strings from a Solr field (may be single value or multi-valued). */
    @SuppressWarnings("unchecked")
    private static List<String> getStringListField(SolrDocument doc, String name) {
        Object val = doc.getFieldValue(name);
        if (val == null) return Collections.emptyList();
        if (val instanceof Collection<?> col) {
            return col.stream().map(String::valueOf).collect(Collectors.toList());
        }
        return List.of(String.valueOf(val));
    }

    private Article toArticle(SolrDocument doc) {
        return Article.builder()
                .id(getStringField(doc, "id"))
                .title(getStringField(doc, "title"))
                .content(getStringField(doc, "content"))
                .author(getStringField(doc, "author"))
                .category(getStringField(doc, "category"))
                .tags(getStringListField(doc, "tags"))
                .createdAt(getStringField(doc, "created_at"))
                .build();
    }
}
