package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogInResponse {
    private String username;
    private String password;
    private String message;
}
