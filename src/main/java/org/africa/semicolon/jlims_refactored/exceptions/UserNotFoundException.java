package org.africa.semicolon.jlims_refactored.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String exception) {
        super(exception);
    }
}
