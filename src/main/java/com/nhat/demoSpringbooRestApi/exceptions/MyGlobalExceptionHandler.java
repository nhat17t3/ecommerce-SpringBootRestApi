package com.nhat.demoSpringbooRestApi.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyGlobalExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> myRuntimeException(RuntimeException e) {
//        String message = e.getMessage();
//        ErrorResponse res = new ErrorResponse("RuntimeException"+message, e.getStackTrace());
//        return new ResponseEntity<ErrorResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> myCustomException(CustomException e) {
        String message = e.getMessage();
        ErrorResponse res = new ErrorResponse("CustomException"+message, null);
        return new ResponseEntity<ErrorResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> myCustomException(ResourceNotFoundException e) {
        String message = e.getMessage();
        ErrorResponse res = new ErrorResponse("ResourceNotFoundException" + message, null);
        return new ResponseEntity<ErrorResponse>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> mapDetailError = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {
            String fieldName = err.getField();
            String message = err.getDefaultMessage();
            mapDetailError.put(fieldName, message);
        });
        ErrorResponse res = new ErrorResponse("validation request DTO Error", mapDetailError);
        return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> myConstraintsVoilationException(ConstraintViolationException e) {
        Map<String, String> mapDetailError = new HashMap<>();
        e.getConstraintViolations().forEach(voilation -> {
            String fieldName = voilation.getPropertyPath().toString();
            String message = voilation.getMessage();
            mapDetailError.put(fieldName, message);
        });
        ErrorResponse res = new ErrorResponse("validation Database Error", mapDetailError);

        return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<CustomException> myMissingPathVariableException(MissingPathVariableException e) {
        CustomException res = new CustomException(e.getMessage());
        return new ResponseEntity<CustomException>(res, HttpStatus.BAD_REQUEST);
    }

}
