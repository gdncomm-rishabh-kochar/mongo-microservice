package com.mongo.mongomicroservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solr")
@Getter
@Setter
public class SolrProperties {

    private String url = "http://localhost:8983/solr";
    private String collection = "articles";
    private String productsCollection = "products";
    private long connectionTimeout = 5000;
    private long readTimeout = 10000;
}
