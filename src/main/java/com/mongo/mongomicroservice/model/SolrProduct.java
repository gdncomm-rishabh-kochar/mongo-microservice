package com.mongo.mongomicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolrProduct {

    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("price")
    private String price;

    @Field("stock")
    private Integer stock;

    @Field("image")
    private String image;

    @Field("indexed_at")
    private String indexedAt;
}
