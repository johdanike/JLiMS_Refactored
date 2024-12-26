package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteUserResponse {
    private String username;
    private String userId;
    private String message;
}
