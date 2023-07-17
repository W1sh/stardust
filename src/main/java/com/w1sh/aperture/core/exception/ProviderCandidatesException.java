package com.w1sh.aperture.core.exception;

public class ProviderCandidatesException extends RuntimeException {

    public <T> ProviderCandidatesException(int numberOfCandidates, Class<T> clazz) {
        super(String.format("Expected 1 candidate but found %d for class %s", numberOfCandidates, clazz.getSimpleName()));
    }
}
