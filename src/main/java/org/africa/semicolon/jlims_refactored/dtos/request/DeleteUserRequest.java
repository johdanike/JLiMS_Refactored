package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.data.models.User;

@Setter
@Getter
public class DeleteUserRequest {
    private String username;
    private String userId;
}
