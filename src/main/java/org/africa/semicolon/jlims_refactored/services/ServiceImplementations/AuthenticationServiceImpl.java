package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;


import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.services.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AccountRegisterResponse registerAccount(AccountRegisterRequest accountRegisterRequest) {
        checkIfUserAlreadyExists(accountRegisterRequest.getUsername());
        User user = new User();
        user.setUsername(accountRegisterRequest.getUsername());
        user.setPassword(accountRegisterRequest.getPassword());
        user.setRole(accountRegisterRequest.getRole());
        user.setEmail(accountRegisterRequest.getEmail());
        user.setLoggedIn(false);
        userRepository.save(user);
        AccountRegisterResponse response = new AccountRegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMessage("Successfully registered");
        return response;
    }

    @Override
    public boolean login(LoginRequest loginRequest) {
        User user = this.userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || loginRequest.getUsername() == null || !user.getPassword().equals(loginRequest.getPassword())

                || !user.getUsername().equals(loginRequest.getUsername())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        user.setLoggedIn(true);
        System.out.println("Logged In Successfully");
        return user.isLoggedIn();
    }

    @Override
    public boolean logout() {
        User user = getCurrentUser();
        user.setLoggedIn(false);
        userRepository.save(user);
        return true;
    }

    private User getCurrentUser() {
        User user = new User();
        if(userRepository.findByUsername(user.getUsername()) != null || user.isLoggedIn()) {
            return userRepository.findByUsername(user.getUsername());
        }
        return user;
    }

    private void checkIfUserAlreadyExists(String username) {
        if(userRepository.findByUsername(username) != null)
            throw new UserAlreadyExistsException("Username " + username + " already exists!");
    }
}
