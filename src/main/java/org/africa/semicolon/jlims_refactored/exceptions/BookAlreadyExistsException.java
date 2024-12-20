package org.africa.semicolon.jlims_refactored.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
