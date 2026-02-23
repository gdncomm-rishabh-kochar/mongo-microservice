package com.mongo.mongomicroservice.exception;

public class SolrOperationException extends RuntimeException {

    public SolrOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrOperationException(String message) {
        super(message);
    }
}
