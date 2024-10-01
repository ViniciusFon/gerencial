package com.gpa.tributario.gerencial.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<Object> businessError(BusinessException ex, WebRequest request) {

		return handleExceptionInternal(ex, ex.buildResponse(), new HttpHeaders(), ex.getStatusCode(), request);
	}

	@ExceptionHandler(DefaultException.class)
	public ResponseEntity<Object> gpaError(DefaultException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, ex.buildResponse(), new HttpHeaders(), ex.getStatusCode(), request);
	}

	@ExceptionHandler(InfrastructureException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> servidorException(InfrastructureException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex.buildResponse(), new HttpHeaders(), ex.getStatusCode(), request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> constrainExcpetion(ConstraintViolationException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> genericExcpetion(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, "Erro interno", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> notFoundError(NotFoundException ex, WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().message("Registro não encontrado").details(ex.getMessage()).build();
		return handleExceptionInternal(ex, responseDefault, new HttpHeaders(), ex.getStatusCode(), request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<Object> accessError(AccessDeniedException ex, WebRequest request) {
		//MessageDto responseDefault = MessageDto.builder().message("Acesso negado").details(ex.getMessage()).build();
		return handleExceptionInternal(ex, "Acesso negado", new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ResponseEntity<Object> credentialsError(BadCredentialsException ex, WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().details(ex.getMessage()).build();
		return handleExceptionInternal(ex, responseDefault, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ResponseEntity<Object> userNotFoundError(InternalAuthenticationServiceException ex, WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().details(ex.getMessage()).build();
		return handleExceptionInternal(ex, responseDefault, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().message("Erro de validação").build();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			responseDefault.addAdditionalMessages(fieldError.getField() + ": " + fieldError.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(responseDefault);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().message("Dados de entrada com problema").details(ex.getMessage()).build();
		return handleExceptionInternal(ex, responseDefault, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
		MessageDto responseDefault = MessageDto.builder().message("Unauthorized").details(ex.getMessage()).build();
		return handleExceptionInternal(ex, responseDefault, new HttpHeaders(), ex.getStatusCode(), request);
	}

}
