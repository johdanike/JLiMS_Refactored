package org.africa.semicolon.jlims_refactored.controllers;

import lombok.RequiredArgsConstructor;
import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.UserApiResponse;
import org.africa.semicolon.jlims_refactored.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountRegisterRequest registerRequest){
        try{
            AccountRegisterResponse registerResponse = userService.register(registerRequest);
            return new ResponseEntity<>(new UserApiResponse(true, registerResponse), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody AddBookRequest addBookRequest){
        try {
            userService.addBook(addBookRequest);
            return new ResponseEntity<>(new UserApiResponse(true, addBookRequest), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/borrowBook")
    public ResponseEntity<?> borrowBook(@RequestParam("bookId") BorrowBookRequest borrowBookRequest){
        try{
            userService.borrowBook(borrowBookRequest);
            return new ResponseEntity<>(new UserApiResponse(true, borrowBookRequest), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/returnBook")
    public ResponseEntity<?> returnBook(@RequestBody ReturnBookRequest returnBookRequest){
        try{
            userService.returnBook(returnBookRequest);
            return new ResponseEntity<>(new UserApiResponse(true, returnBookRequest), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteBook")
    public ResponseEntity<?> deleteBook(@RequestBody DeleteBookRequest deleteBookRequest){
        try{
            userService.deleteBook(deleteBookRequest);
            return new ResponseEntity<>(new UserApiResponse(true, deleteBookRequest), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
