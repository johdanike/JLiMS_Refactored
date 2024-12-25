package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.*;

public interface UserService {
    AccountRegisterResponse register(AccountRegisterRequest registerRequest);

    //    LogInResponse logIn(LoginRequest loginRequest);
//    LogOutResponse logOut(LogOutRequest logOutRequest);
    AddBookResponse addBook(AddBookRequest addBookRequest);
    BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest);
    ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest);
    DeleteBookResponse deleteBook(DeleteBookRequest deleteBookRequest);

}
