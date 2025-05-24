package com.genesisresources.genesisregistrationsystem.exception;

public class PersonIdAlreadyExistsException extends RuntimeException {
    public PersonIdAlreadyExistsException(String message) {
        super(message);
    }
}