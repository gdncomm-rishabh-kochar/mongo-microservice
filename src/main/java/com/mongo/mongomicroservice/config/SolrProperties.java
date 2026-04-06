package com.mongo.mongomicroservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solr")
@Getter
@Setter
public class SolrProperties {

    private boolean enabled = true;
    private String url = "http://localhost:8983/solr";
    private String collection = "articles";
    private String productsCollection = "products";
    private int connectionTimeout = 5000;
    private int readTimeout = 10000;
}
