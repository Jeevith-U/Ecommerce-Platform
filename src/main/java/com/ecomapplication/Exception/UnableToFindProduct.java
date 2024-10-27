package com.ecomapplication.Exception;

public class UnableToFindProduct extends RuntimeException {

    public UnableToFindProduct() {}

    public UnableToFindProduct(String message) {super(message);}
}
