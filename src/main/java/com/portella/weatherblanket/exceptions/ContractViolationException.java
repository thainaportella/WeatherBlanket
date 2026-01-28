package com.portella.weatherblanket.exceptions;

public class ContractViolationException extends RuntimeException {

    private final int status;
    private final String error;

    public ContractViolationException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}

