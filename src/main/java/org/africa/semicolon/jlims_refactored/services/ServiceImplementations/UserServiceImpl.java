package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.Books;
import org.africa.semicolon.jlims_refactored.data.repositories.Libraries;
import org.africa.semicolon.jlims_refactored.data.repositories.Users;
import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.*;
import org.africa.semicolon.jlims_refactored.exceptions.BookAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.exceptions.BookDetailsCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNameFieldCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNotFoundException;
import org.africa.semicolon.jlims_refactored.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private Users users;
    @Autowired
    private Books books;
    @Autowired
    private Libraries libraries;


    @Override
    public AccountRegisterResponse register(AccountRegisterRequest registerRequest) {
        usernameValidator(registerRequest);

        User user = getUserDetailsForRegistration(registerRequest);

        AccountRegisterResponse response = new AccountRegisterResponse();
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setMessage("User created successfully");
        return response;
    }

    @Override
    public AddBookResponse addBook(AddBookRequest addBookRequest) {
        bookExistsValidator(addBookRequest.getTitle(), addBookRequest.getAuthor());
        Book book = getBookDetailsForBookStocking(addBookRequest);

        AddBookResponse response = new AddBookResponse();
        response.setBookName(book.getTitle());
        response.setBookId(book.getId());
        response.setMessage("Book added successfully");
        return response;
    }

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        User registeredMember = findUserByUsername(borrowBookRequest.getUsername());
        usernameValidator(registeredMember);


        //find book id
        Optional<Book> bookId = findBookById(borrowBookRequest.getBookId());
        System.out.println("book id is: "+bookId);


        BorrowBookResponse response = new BorrowBookResponse();
        System.out.println("Another id is: "+findBookById(response.getBookId()));
        response.setBookId(borrowBookRequest.getBookId());
        response.setName(borrowBookRequest.getUsername());
        response.setRole(borrowBookRequest.getRole());
        response.setAuthor(borrowBookRequest.getAuthor());
        response.setBookName(borrowBookRequest.getBookName());
        response.setMessage("Book borrowed successfully");

        Library bookLoan = new Library();
        bookLoan.setId(borrowBookRequest.getBookId());



//        checkIfBookIsInStock(findBookById(borrowBookRequest.getBookId()));
        return new BorrowBookResponse();
    }

    private Optional<Book> findBookById(String bookId) {
        return books.findById(bookId);
    }

    private User findUserByUsername(String username) {
        if(username != null) {
            return users.findByUsername(username);
        }
        throw new UserNotFoundException("User not found!");
    }

    @Override
    public ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest) {
        return null;
    }

    @Override
    public DeleteBookResponse deleteBook(DeleteBookRequest deleteBookRequest) {
        return null;
    }

    private void bookExistsValidator(String title, String author) {
        if(title == null || author == null || author.trim().isEmpty()) {
            throw new BookDetailsCannotBeEmptyException("Title cannot be null");
        } else if (title.equalsIgnoreCase(books.findByTitle(title)) && author.equalsIgnoreCase(books.findByAuthor(author))) {
            throw new BookAlreadyExistsException("Book already exists");
        }
    }

    private User getUserDetailsForRegistration(AccountRegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());
        user.setLoggedIn(false);
        users.save(user);
        return user;
    }


    private Book getBookDetailsForBookStocking(AddBookRequest addBookRequest) {
        Book book = new Book();

        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setGenre(addBookRequest.getGenre());
        books.save(book);

        return book;
    }

    private void usernameValidator(AccountRegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            throw new UserNameFieldCannotBeEmptyException("Username cannot be null");
        }
    }

    private void usernameValidator(User user){
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new UserNameFieldCannotBeEmptyException("Username cannot be null");
        }
    }
}
