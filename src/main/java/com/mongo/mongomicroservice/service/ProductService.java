package com.mongo.mongomicroservice.service;

import com.mongo.mongomicroservice.dto.request.ProductRequest;
import com.mongo.mongomicroservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse findById(String id);

    List<ProductResponse> findAll();

    ProductResponse update(String id, ProductRequest request);

    void delete(String id);
}
