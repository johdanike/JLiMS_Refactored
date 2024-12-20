package org.africa.semicolon.jlims.exceptions;

public class UserNameFieldCannotBeEmptyException extends RuntimeException {
    public UserNameFieldCannotBeEmptyException(String message) {
        super(message);
    }
}
