package com.mongo.mongomicroservice.dto.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductKafkaMessage {

    private String name;
    private BigDecimal price;
    private Integer stock;
    private String image;
}
