package com.mongo.mongomicroservice.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(prefix = "solr", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SolrProperties.class)
public class SolrConfig {

    @Bean(destroyMethod = "close")
    public SolrClient solrClient(SolrProperties properties) {
        return new HttpSolrClient.Builder(properties.getUrl())
                .withConnectionTimeout(properties.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .withSocketTimeout(properties.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
}
