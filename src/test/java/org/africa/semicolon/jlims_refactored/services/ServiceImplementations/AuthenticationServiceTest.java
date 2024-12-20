package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.repositories.Users;
import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    AccountRegisterRequest accountRegisterRequest;

    @Autowired
    Users users;

    @BeforeEach
    public void setUp() {
        users.deleteAll();

        accountRegisterRequest = new AccountRegisterRequest();
        accountRegisterRequest.setUsername("username");
        accountRegisterRequest.setPassword("password");
        accountRegisterRequest.setEmail("email@email.com");
        accountRegisterRequest.setCreatedAt(LocalDateTime.now());
        accountRegisterRequest.setLoggedIn(accountRegisterRequest.isLoggedIn());
        accountRegisterRequest.setRegistered(true);
        accountRegisterRequest.setName("John-Daniel Ikechukwu");
    }

    @Test
    public void testRegisterAccountAccount() {
        accountRegisterRequest.setLoggedIn(false);
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);
        assertEquals(1, users.count());
    }

    @Test
    public void test_thatUserCanLogin() {
        accountRegisterRequest.setLoggedIn(false);
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(accountRegisterRequest.getUsername());
        loginRequest.setPassword(accountRegisterRequest.getPassword());
        boolean loginResponse = authenticationService.login(loginRequest);
        assertTrue(loginResponse);
        assertEquals(1, users.count());
    }


    @Test
    public void test_thatUserCanLogOut() {
        accountRegisterRequest.setLoggedIn(false);
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(accountRegisterRequest.getUsername());
        loginRequest.setPassword(accountRegisterRequest.getPassword());
        boolean loginResponse = authenticationService.login(loginRequest);
        assertTrue(loginResponse);
        boolean logoutResponse = authenticationService.logout();
        assertTrue(logoutResponse);
    }

    @Test
    public void test_thatUserCannotLoginWithWrongUsername_throwsException() {
        accountRegisterRequest.setLoggedIn(false);
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Lagbaja");
        loginRequest.setPassword("password");

        IllegalArgumentException throwException = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.login(loginRequest);
        });

        assertEquals("Invalid username or password", throwException.getMessage());
    }

    @Test
    public void test_thatUserCannotRegisterAccountSameNameMoreThanOnce_throwsException() {
        accountRegisterRequest.setLoggedIn(false);
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        UserAlreadyExistsException throwException = assertThrows(UserAlreadyExistsException.class, () -> {
            AccountRegisterResponse accountRegisterResponse2 = authenticationService.registerAccount(accountRegisterRequest);
            assertNotNull(accountRegisterResponse2);
        });
        assertTrue(throwException.getMessage().contains("Username " + accountRegisterResponse.getUsername() + " already exists!"));
    }


}