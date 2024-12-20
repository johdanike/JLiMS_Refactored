package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;

public interface AuthenticationService {
    AccountRegisterResponse registerAccount(AccountRegisterRequest accountRegisterRequest);
    boolean login(LoginRequest loginRequest);
    boolean logout();

}
