package com.gpa.tributario.gerencial.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends DefaultException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
