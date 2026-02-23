package com.mongo.mongomicroservice.listener;

import com.mongo.mongomicroservice.dto.kafka.ProductKafkaMessage;
import com.mongo.mongomicroservice.model.Product;
import com.mongo.mongomicroservice.model.SolrProduct;
import com.mongo.mongomicroservice.repository.ProductRepository;
import com.mongo.mongomicroservice.repository.SolrProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final ProductRepository productRepository;
    private final SolrProductRepository solrProductRepository;

    @KafkaListener(
            topics = "${kafka.topic.product}",
            containerFactory = "productKafkaListenerContainerFactory"
    )
    public void onProductEvent(ProductKafkaMessage message) {
        log.debug("Received product event: {}", message);

        Product savedProduct = saveToMongo(message);
        indexToSolr(savedProduct);

        log.debug("Product {} persisted to MongoDB and indexed in Solr", savedProduct.getId());
    }

    private Product saveToMongo(ProductKafkaMessage message) {
        Product product = Product.builder()
                .name(message.getName())
                .price(message.getPrice())
                .stock(message.getStock())
                .image(message.getImage())
                .build();
        Product saved = productRepository.save(product);
        log.debug("Saved product to MongoDB with id: {}", saved.getId());
        return saved;
    }

    private void indexToSolr(Product product) {
        SolrProduct solrProduct = SolrProduct.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice() != null ? product.getPrice().toPlainString() : null)
                .stock(product.getStock())
                .image(product.getImage())
                .indexedAt(Instant.now().toString())
                .build();
        solrProductRepository.save(solrProduct);
        log.debug("Indexed product in Solr with id: {}", product.getId());
    }
}
