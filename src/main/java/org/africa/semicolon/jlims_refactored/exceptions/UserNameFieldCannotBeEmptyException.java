package org.africa.semicolon.jlims_refactored.exceptions;

public class UserNameFieldCannotBeEmptyException extends RuntimeException {
    public UserNameFieldCannotBeEmptyException(String message) {
        super(message);
    }
}
