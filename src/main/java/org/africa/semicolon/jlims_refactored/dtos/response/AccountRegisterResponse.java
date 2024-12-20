package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRegisterResponse {
    private String id;
    private String username;
    private String message;
}
