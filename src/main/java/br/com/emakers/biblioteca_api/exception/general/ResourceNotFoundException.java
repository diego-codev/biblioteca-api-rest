package br.com.emakers.biblioteca_api.exception.general;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
