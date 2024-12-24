package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.Books;
import org.africa.semicolon.jlims_refactored.data.repositories.Inventories;
import org.africa.semicolon.jlims_refactored.data.repositories.Libraries;
import org.africa.semicolon.jlims_refactored.data.repositories.Users;
import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.*;
import org.africa.semicolon.jlims_refactored.exceptions.BookAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.exceptions.BookDetailsCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNameFieldCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNotFoundException;
import org.africa.semicolon.jlims_refactored.services.InventoryService;
import org.africa.semicolon.jlims_refactored.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Autowired
    private InventoryService inventories;


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
        response.setBookQuantity(book.getNumOfCopies());
        response.setMessage("Book added successfully");
        return response;
    }

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        User registeredMember = findUserByUsername(borrowBookRequest.getUsername());
        usernameValidator(registeredMember);

        int numOfCopiesAvailable = newBookQuantityAfterBorrowing(borrowBookRequest);

        boolean bookId = findBookById(borrowBookRequest.getBookId());
        System.out.println("book id is: "+bookId);
        System.out.println(borrowBookRequest.getBookId());

        BorrowBookResponse response = new BorrowBookResponse();
        response.setBookId(borrowBookRequest.getBookId());
        response.setName(borrowBookRequest.getUsername());
        response.setRole(borrowBookRequest.getRole());
        response.setAuthor(borrowBookRequest.getAuthor());
        response.setBookName(borrowBookRequest.getBookName());
        response.setMessage("Book borrowed successfully");
        response.setQuantity(numOfCopiesAvailable);
        response.setBorrowDate(borrowBookRequest.getBorrowDate());

        Inventory inventory = new Inventory();
        inventory.setBorrowed(true);
        inventory.setBookId(borrowBookRequest.getBookId());
        inventory.setUserId(registeredMember.getId());
        inventory.setDateBorrowed(borrowBookRequest.getBorrowDate());
        inventory.setDateReturned(null);
        inventory.setNoOfCopyOfBooks(numOfCopiesAvailable);
        inventories.save(inventory);


        return response;
    }

    private boolean findBookById(String bookId) {
        return books.existsById(bookId);
    }

    private int newBookQuantityAfterBorrowing(BorrowBookRequest borrowBookRequest){
        bookExistsValidator(borrowBookRequest.getTitle(), borrowBookRequest.getAuthor());
        int presentNumOfCopies = 0;
        Optional<Book> book = books.findById(borrowBookRequest.getBookId());

        if (book.isPresent()){
            presentNumOfCopies = book.get().getNumOfCopies();
        }
        if (presentNumOfCopies <= 0)
            throw new IllegalArgumentException("Book is currently out of stock, " +
                    "mind checking another book?");

        book.get().setNumOfCopies(presentNumOfCopies - 1);
//        System.out.println("new number of copieis" + book.get().getNumOfCopies());

        books.save(book.get());
        return book.get().getNumOfCopies();
    }

    private User findUserByUsername(String username) {
        if(username != null) {
            return users.findByUsername(username);
        }
        throw new UserNotFoundException("User not found!");
    }

    @Override
    public ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest) {

        User registeredMember = findUserByUsername(returnBookRequest.getUsername());
        usernameValidator(registeredMember);

        int numOfCopiesAvailable = newBookQuantityAfterReturning(returnBookRequest);
        System.out.println("no of copies" + numOfCopiesAvailable);

        ReturnBookResponse response = new ReturnBookResponse();
        response.setBookId(returnBookRequest.getBookId());
        response.setQuantity(numOfCopiesAvailable);
        response.setUsername(returnBookRequest.getUsername());
        response.setMessage("Book returned successfully");

        BorrowBookResponse borrowBookResponse = new BorrowBookResponse();
        Inventory inventory = new Inventory();
        inventory.setBorrowed(false);
        inventory.setUserId(registeredMember.getId());
        inventory.setNoOfCopyOfBooks(numOfCopiesAvailable);
        inventory.setDateReturned(LocalDate.now());
        inventory.setReturned(true);
        inventory.setDateBorrowed(borrowBookResponse.getBorrowDate());
        inventory.setBookId(returnBookRequest.getBookId());

        inventories.save(inventory);

        return response;
    }

    private int newBookQuantityAfterReturning(ReturnBookRequest returnBookRequest) {
        bookExistsValidator(returnBookRequest.getBookName(), returnBookRequest.getAuthor());
        int presentNumOfCopies = 0;
        System.out.println("olodo" + returnBookRequest.getBookId());
        Optional<Book> book = books.findById(returnBookRequest.getBookId());
        System.out.println("numbwr "+book.get().getNumOfCopies());

        System.out.println("book"+ book.get().getNumOfCopies());
        System.out.println("book"+ book.get().getTitle());

        Book foundBook = book.orElseThrow(() -> new IllegalArgumentException("Book " + returnBookRequest.getBookId()+" not found!"));
        presentNumOfCopies = foundBook.getNumOfCopies();
        foundBook.setNumOfCopies(presentNumOfCopies + 1);
        System.out.println("new number of copieis" + book.get().getNumOfCopies());

        books.save(foundBook);
        return foundBook.getNumOfCopies();
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
        book.setNumOfCopies(addBookRequest.getNoOfCopies() != null ? addBookRequest.getNoOfCopies() : 0);
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
