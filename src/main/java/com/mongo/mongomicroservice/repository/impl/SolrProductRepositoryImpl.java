package com.mongo.mongomicroservice.repository.impl;

import com.mongo.mongomicroservice.config.SolrProperties;
import com.mongo.mongomicroservice.exception.SolrOperationException;
import com.mongo.mongomicroservice.model.SolrProduct;
import com.mongo.mongomicroservice.repository.SolrProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@ConditionalOnProperty(prefix = "solr", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class SolrProductRepositoryImpl implements SolrProductRepository {

    private final SolrClient solrClient;
    private final SolrProperties solrProperties;

    @Override
    public SolrProduct save(SolrProduct solrProduct) {
        try {
            solrClient.addBean(solrProperties.getProductsCollection(), solrProduct);
            solrClient.commit(solrProperties.getProductsCollection());
            return solrProduct;
        } catch (SolrServerException | IOException e) {
            throw new SolrOperationException(
                    "Failed to index product in Solr with id: " + solrProduct.getId(), e);
        }
    }
}
