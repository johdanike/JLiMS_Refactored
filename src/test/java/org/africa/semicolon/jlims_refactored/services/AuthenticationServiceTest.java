package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    AccountRegisterRequest accountRegisterRequest;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        accountRegisterRequest = new AccountRegisterRequest();
        accountRegisterRequest.setUsername("username");
        accountRegisterRequest.setPassword("password");
        accountRegisterRequest.setEmail("email@email.com");
        accountRegisterRequest.setRegistered(true);
        accountRegisterRequest.setLoggedIn(false);
    }

    @Test
    public void testRegisterAccountAccount() {
        AccountRegisterResponse accountRegisterResponse =
                authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);
        assertEquals(1, userRepository.count());
    }

    @Test
    public void test_thatUserCanLogin() {
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(accountRegisterRequest.getUsername());
        loginRequest.setPassword(accountRegisterRequest.getPassword());
        boolean loginResponse = authenticationService.login(loginRequest);
        assertTrue(loginResponse);
        assertEquals(1, userRepository.count());
    }


    @Test
    public void test_thatUserCanLogOut() {
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
        AccountRegisterResponse accountRegisterResponse = authenticationService.registerAccount(accountRegisterRequest);
        assertNotNull(accountRegisterResponse);

        UserAlreadyExistsException throwException = assertThrows(UserAlreadyExistsException.class, () -> {
            AccountRegisterResponse accountRegisterResponse2 = authenticationService.registerAccount(accountRegisterRequest);
            assertNotNull(accountRegisterResponse2);
        });
        assertTrue(throwException.getMessage().contains("Username " + accountRegisterResponse.getUsername() + " already exists!"));
    }


}