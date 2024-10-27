package com.ecomapplication.Exception;

import com.ecomapplication.Util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryExistException.class)
    public ResponseEntity<CategoryExistException> categoryExistException(CategoryExistException e) {
        String message = e.getMessage();
        return  new ResponseEntity<>(e, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIException> resourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        APIException apiException = new APIException(message);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ResponseStructure> myAPIException(APIException e) {
        String message = e.getMessage();
        ResponseStructure structure = new ResponseStructure(message, false, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
    }
}
