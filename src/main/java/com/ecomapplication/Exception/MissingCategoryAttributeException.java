package com.ecomapplication.Exception;

public class MissingCategoryAttributeException extends RuntimeException {

    public MissingCategoryAttributeException() {}

    public MissingCategoryAttributeException(String message) {super(message);}
}
