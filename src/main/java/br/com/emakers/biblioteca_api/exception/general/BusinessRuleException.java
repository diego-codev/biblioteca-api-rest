package br.com.emakers.biblioteca_api.exception.general;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) { super(message); }
}
