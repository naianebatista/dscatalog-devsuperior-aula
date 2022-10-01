package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHander {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status= HttpStatus.NOT_FOUND;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());  //gera  o codigo 404
		err.setError("Resource Not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());     //pega o caminho da requisicao
		return ResponseEntity.status(status.NOT_FOUND).body(err);    //.status customiza o status q vai retornar
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
		HttpStatus status= HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.BAD_REQUEST.value());  //gera  o codigo 400
		err.setError("Database Exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());     //pega o caminho da requisicao
		return ResponseEntity.status(status.NOT_FOUND).body(err);

	}
}
