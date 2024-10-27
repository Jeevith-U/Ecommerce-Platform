package com.ecomapplication.Exception;

public class ProductExistException extends RuntimeException {

    public ProductExistException() {}

    public ProductExistException(String message) {super(message);}
}
