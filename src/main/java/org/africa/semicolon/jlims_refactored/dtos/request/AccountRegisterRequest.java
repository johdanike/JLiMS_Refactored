package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Role;


@Setter
@Getter
public class AccountRegisterRequest {
    private String username;
    private String password;
    private Role role;
    private boolean isLoggedIn;
    private boolean isRegistered;
    private String email;
}
