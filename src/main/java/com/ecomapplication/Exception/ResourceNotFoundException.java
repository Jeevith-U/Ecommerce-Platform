package com.ecomapplication.Exception;

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;
    String fieldId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String fieldName,  String fieldId) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldName));
        this.resourceName = resourceName;
        this.fieldId = fieldId;
        this.fieldName = fieldName;
    }
}
