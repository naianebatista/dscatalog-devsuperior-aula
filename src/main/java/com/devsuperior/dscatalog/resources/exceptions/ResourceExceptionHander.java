package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHander {
	 
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
		
		 StandardError err =new StandardError();
		 
		 err.setTimestamp(Instant.now());
		 err.setStatus(HttpStatus.NOT_FOUND.value());  //gera  o codigo 404
		 err.setError("Resource Not Found");
		 err.setMessage(e.getMessage());
		 err.setPath(request.getRequestURI());  	 //pega o caminho da requisicao
		 
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); 	//.status customiza o status q vai retornar
				
	}

}
