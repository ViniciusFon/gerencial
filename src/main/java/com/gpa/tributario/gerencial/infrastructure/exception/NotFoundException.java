package com.gpa.tributario.gerencial.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DefaultException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
