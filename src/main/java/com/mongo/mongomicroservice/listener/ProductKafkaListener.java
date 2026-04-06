package com.mongo.mongomicroservice.listener;

import com.mongo.mongomicroservice.config.SolrProperties;
import com.mongo.mongomicroservice.dto.kafka.ProductKafkaMessage;
import com.mongo.mongomicroservice.model.Product;
import com.mongo.mongomicroservice.model.SolrProduct;
import com.mongo.mongomicroservice.repository.ProductRepository;
import com.mongo.mongomicroservice.repository.SolrProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final ProductRepository productRepository;
    private final Optional<SolrProductRepository> solrProductRepository;
    private final SolrProperties solrProperties;

    @KafkaListener(
            topics = "${kafka.topic.product}",
            containerFactory = "productKafkaListenerContainerFactory"
    )
    public void onProductEvent(ProductKafkaMessage message) {
        log.info("Received product event: {}", message);

        Product savedProduct = saveToMongo(message);
        indexToSolr(savedProduct);

        if (solrProperties.isEnabled()) {
            log.info("Product {} persisted to MongoDB and indexed in Solr", savedProduct.getId());
        } else {
            log.info("Product {} persisted to MongoDB (Solr indexing disabled)", savedProduct.getId());
        }
    }

    private Product saveToMongo(ProductKafkaMessage message) {
        Product product = Product.builder()
                .name(message.getName())
                .price(message.getPrice())
                .stock(message.getStock())
                .image(message.getImage())
                .build();
        Product saved = productRepository.save(product);
        log.info("Saved product to MongoDB with id: {}", saved.getId());
        return saved;
    }

    private void indexToSolr(Product product) {
        if (!solrProperties.isEnabled() || solrProductRepository.isEmpty()) {
            log.info("Skipping Solr indexing for product {} because Solr is disabled", product.getId());
            return;
        }

        SolrProduct solrProduct = SolrProduct.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice() != null ? product.getPrice().toPlainString() : null)
                .stock(product.getStock())
                .image(product.getImage())
                .indexedAt(Instant.now().toString())
                .build();
        solrProductRepository.get().save(solrProduct);
        log.info("Indexed product in Solr with id: {}", product.getId());
    }
}
