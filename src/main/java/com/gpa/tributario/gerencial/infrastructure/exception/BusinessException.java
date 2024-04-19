package com.gpa.tributario.gerencial.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BusinessException extends DefaultException {

    private static final long serialVersionUID = -3253005617045010385L;

    private final List<MessageDto> additionalMessages;

    public BusinessException(final String message) {
        super(message);
        this.additionalMessages = Collections.emptyList();
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
        this.additionalMessages = Collections.emptyList();
    }

    public BusinessException(final String message, final List<MessageDto> erros) {
        super(message);
        this.additionalMessages = erros;
    }

    public BusinessException(final List<MessageDto> erros) {
        super();
        this.additionalMessages = erros;
    }

    public BusinessException(MessageDto... erros) {
        this.additionalMessages = Objects.nonNull(erros) ? Arrays.asList(erros) : null;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public List<MessageDto> getAdditionalMessages() {
        return additionalMessages;
    }
}
