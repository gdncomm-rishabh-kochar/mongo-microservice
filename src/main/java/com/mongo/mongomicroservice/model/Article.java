package com.mongo.mongomicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Field("id")
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("author")
    private String author;

    @Field("category")
    private String category;

    @Field("tags")
    private List<String> tags;

    @Field("created_at")
    private String createdAt;
}
