package org.africa.semicolon.jlims.exceptions;

public class BookDetailsCannotBeEmptyException extends RuntimeException {
    public BookDetailsCannotBeEmptyException(String message) {
        super(message);
    }
}
