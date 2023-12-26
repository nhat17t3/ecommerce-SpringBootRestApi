package com.nhat.demoSpringbooRestApi.exceptions;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
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

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<BaseResponse> myException(Exception e) {
//        BaseResponse baseResponse = new BaseResponse(false,"Exception: "+e.getMessage(), null,e.getStackTrace());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
//    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse> myCustomException(CustomException e) {
        BaseResponse baseResponse = new BaseResponse(false,"Custom Exception: "+e.getMessage(), null,e.getStackTrace()[0]);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse> myCustomException(ResourceNotFoundException e) {
        String message = e.getMessage();
        BaseResponse baseResponse = new BaseResponse(false,"ResourceNotFoundException: "+e.getMessage(), null,e.getStackTrace()[0]);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> mapDetailError = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {
            String fieldName = err.getField();
            String message = err.getDefaultMessage();
            mapDetailError.put(fieldName, message);
        });
        BaseResponse baseResponse = new BaseResponse(false,"validation request DTO Error: "+e.getMessage(), null,mapDetailError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> myConstraintsVoilationException(ConstraintViolationException e) {
        Map<String, String> mapDetailError = new HashMap<>();
        e.getConstraintViolations().forEach(voilation -> {
            String fieldName = voilation.getPropertyPath().toString();
            String message = voilation.getMessage();
            mapDetailError.put(fieldName, message);
        });
        BaseResponse baseResponse = new BaseResponse(false,"Validation Database Error: "+e.getMessage(), null,mapDetailError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<BaseResponse> myMissingPathVariableException(MissingPathVariableException e) {
        BaseResponse baseResponse = new BaseResponse(false,"MissingPathVariableException: "+e.getMessage(), null,e.getStackTrace()[0]);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

}
