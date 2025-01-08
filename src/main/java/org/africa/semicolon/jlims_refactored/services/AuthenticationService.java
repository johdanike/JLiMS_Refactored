package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LogoutRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.LogInResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.LogOutResponse;

public interface AuthenticationService {
    AccountRegisterResponse registerAccount(AccountRegisterRequest accountRegisterRequest);
    LogInResponse login(LoginRequest loginRequest);
    LogOutResponse logout(LogoutRequest logoutRequest);
}
