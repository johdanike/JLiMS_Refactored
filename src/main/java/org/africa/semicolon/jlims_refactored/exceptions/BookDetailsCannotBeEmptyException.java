package org.africa.semicolon.jlims_refactored.exceptions;

public class BookDetailsCannotBeEmptyException extends RuntimeException {
    public BookDetailsCannotBeEmptyException(String message) {
        super(message);
    }
}
