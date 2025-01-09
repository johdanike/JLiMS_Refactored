package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;


import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LoginRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.LogoutRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.LogInResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.LogOutResponse;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNotFoundException;
import org.africa.semicolon.jlims_refactored.services.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AccountRegisterResponse registerAccount(AccountRegisterRequest accountRegisterRequest) {
        checkThatFieldIsNotNull(accountRegisterRequest);
        checkIfUserAlreadyExists(accountRegisterRequest.getUsername());

        User user = getUserDetailsForRegistration(accountRegisterRequest);

        AccountRegisterResponse response = new AccountRegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMessage("Successfully registered");
        return response;
    }

    private User getUserDetailsForRegistration(AccountRegisterRequest accountRegisterRequest) {
        User user = new User();

        user.setUsername(accountRegisterRequest.getUsername());
        user.setPassword(accountRegisterRequest.getPassword());
        user.setRole(accountRegisterRequest.getRole());
        user.setEmail(accountRegisterRequest.getEmail());
        user.setLoggedIn(false);
        userRepository.save(user);

        Inventory inventory = new Inventory();
        inventory.setUserId(user.getId());
        return user;
    }

    private static void checkThatFieldIsNotNull(AccountRegisterRequest accountRegisterRequest) {
        if (containsWhiteSpace(accountRegisterRequest.getUsername()) ||
                containsWhiteSpace(accountRegisterRequest.getPassword()) ||
                containsWhiteSpace(accountRegisterRequest.getEmail())){
            throw new IllegalArgumentException("Field cannot contain white spaces");
        }
        isAccountRegisterFieldEmptyOrNull(accountRegisterRequest);
    }

    private static void isAccountRegisterFieldEmptyOrNull(AccountRegisterRequest accountRegisterRequest) {
        if (isRegisterDetailsOfNullValue(accountRegisterRequest) ||
                isRegisterDetailAnEmptyStringSet(accountRegisterRequest)) {
            throw new IllegalArgumentException("Fields cannot be empty");
        }
    }

    private static boolean containsWhiteSpace(String string){
        Pattern pattern = Pattern.compile("(.*?)\\s(.*?)");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    private static boolean isRegisterDetailsOfNullValue(AccountRegisterRequest accountRegisterRequest) {
        if (accountRegisterRequest.getPassword() == null ||
                accountRegisterRequest.getUsername() == null ||
                accountRegisterRequest.getEmail() == null
        )
            throw new IllegalArgumentException("Fields cannot be empty");
        return false;
    }

    private static boolean isRegisterDetailAnEmptyStringSet(AccountRegisterRequest accountRegisterRequest){
        if (accountRegisterRequest.getPassword().trim().equals("")
                || accountRegisterRequest.getUsername().trim().equals("")
                || accountRegisterRequest.getEmail().trim().equals("") ){
            throw new IllegalArgumentException("Fields cannot be empty");
        }
        return false;
    }

    @Override
    public LogInResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (isLoginRequestEmptyOrNull(loginRequest) ||
                containsWhiteSpace(loginRequest.getPassword()) ||
                containsWhiteSpace(loginRequest.getUsername())) {
            throw new IllegalArgumentException("Username or password fields cannot be empty and cannot contain any spaces");
        }
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }

        LogInResponse response = userDetailsIsCorrect_login(loginRequest, user);
        if (response != null) return response;
        throw new IllegalArgumentException("Invalid username or password");
    }

    private static LogInResponse userDetailsIsCorrect_login(LoginRequest loginRequest, User user) {
        if (user.getUsername().equals(loginRequest.getUsername()) && user.getPassword().equals(loginRequest.getPassword())) {
            user.setLoggedIn(true);
            LogInResponse response = new LogInResponse();
            response.setUsername(user.getUsername());
            response.setMessage("Logged In Successfully");

            return response;
        }
        return null;
    }

    private static boolean isLoginRequestEmptyOrNull(LoginRequest loginRequest) {
        return loginRequest.getPassword() == null || loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty();
    }

    @Override
    public LogOutResponse logout(LogoutRequest logoutRequest){
        User user = getCurrentUser(logoutRequest.getUsername());
        user.setLoggedIn(false);
        LogOutResponse logOutResponse = new LogOutResponse();
        logOutResponse.setMessage("Logout successful");
        userRepository.save(user);
        return logOutResponse;
    }

    private User getCurrentUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user;
    }

    private void checkIfUserAlreadyExists(String username) {
        if(userRepository.findByUsername(username) != null)
            throw new UserAlreadyExistsException("Username " + username + " already exists!");
    }
}
