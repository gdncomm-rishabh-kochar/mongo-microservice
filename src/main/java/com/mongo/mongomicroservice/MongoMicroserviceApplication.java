package com.mongo.mongomicroservice;

import com.mongo.mongomicroservice.config.SolrProperties;
import com.mongo.mongomicroservice.config.RedisPublisherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        SolrProperties.class,
        RedisPublisherProperties.class
})
public class MongoMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoMicroserviceApplication.class, args);
    }
}
