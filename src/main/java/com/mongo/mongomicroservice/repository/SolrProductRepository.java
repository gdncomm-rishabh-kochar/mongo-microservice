package com.mongo.mongomicroservice.repository;

import com.mongo.mongomicroservice.model.SolrProduct;

public interface SolrProductRepository {

    SolrProduct save(SolrProduct solrProduct);
}
