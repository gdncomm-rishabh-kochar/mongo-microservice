package com.mongo.mongomicroservice.service.impl;

import com.mongo.mongomicroservice.dto.request.ProductRequest;
import com.mongo.mongomicroservice.dto.response.ProductResponse;
import com.mongo.mongomicroservice.exception.ResourceNotFoundException;
import com.mongo.mongomicroservice.model.Product;
import com.mongo.mongomicroservice.repository.ProductRepository;
import com.mongo.mongomicroservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .image(request.getImage())
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse findById(String id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse update(String id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setImage(request.getImage());
        return toResponse(productRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImage(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
