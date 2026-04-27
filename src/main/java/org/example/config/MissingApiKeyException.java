package org.example.config;

public class MissingApiKeyException extends RuntimeException {

    public MissingApiKeyException(String message) {
        super(message);
    }
}
