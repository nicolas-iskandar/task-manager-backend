package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found",
				"The requested URL was not found on this server");
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
