package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Role;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccountRegisterRequest {
    private String name;
    private String email;
    private String username;
    private String password;
    private Role role;
    private boolean isLoggedIn;
    private boolean isRegistered;
    private LocalDateTime createdAt;
}
