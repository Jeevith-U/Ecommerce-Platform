package com.ecomapplication.Exception;

public class MissingProductAttributeException extends RuntimeException {

    public MissingProductAttributeException() {}

    public MissingProductAttributeException(String message) {super(message);}
}
