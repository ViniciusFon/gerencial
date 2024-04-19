package com.gpa.tributario.gerencial.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InfrastructureException extends DefaultException {

	private static final long serialVersionUID = 5824145276269516144L;
	private final HttpStatus status;

	public InfrastructureException(final String message) {
		super(message);
		status = null;
	}

	public InfrastructureException(final String message, final HttpStatus status) {
		super(message);
		this.status = status;
	}

	public InfrastructureException(final Throwable cause) {
		super(cause);
		status = null;
	}

	public InfrastructureException(final String message, final Throwable cause) {
		super(message, cause);
		status = null;
	}

	@Override
	public HttpStatus getStatusCode() {
		return status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
	}
}
