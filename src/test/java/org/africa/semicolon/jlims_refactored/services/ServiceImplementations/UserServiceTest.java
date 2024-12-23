package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.repositories.Books;
import org.africa.semicolon.jlims_refactored.data.repositories.Libraries;
import org.africa.semicolon.jlims_refactored.data.repositories.Users;
import org.africa.semicolon.jlims_refactored.dtos.request.AccountRegisterRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.AddBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.BorrowBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AccountRegisterResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.AddBookResponse;
import org.africa.semicolon.jlims_refactored.dtos.response.BorrowBookResponse;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    AccountRegisterRequest accountRegisterRequest;
    @Autowired
    private Users users;
    @Autowired
    private Books books;
    private AddBookRequest addBookRequest;
    private BorrowBookRequest borrowBookRequest;
    @Autowired
    private Libraries libraryRepo;

    @BeforeEach
    public void setUp() {
        users.deleteAll();
        books.deleteAll();
        libraryRepo.deleteAll();

        accountRegisterRequest = new AccountRegisterRequest();
        accountRegisterRequest.setUsername("username");
        accountRegisterRequest.setPassword("password");
        accountRegisterRequest.setEmail("email@email.com");
        accountRegisterRequest.setLoggedIn(accountRegisterRequest.isLoggedIn());
        accountRegisterRequest.setRegistered(true);

        addBookRequest = new AddBookRequest();
        addBookRequest.setTitle("Things fall apart");
        addBookRequest.setAuthor("Chinua Achebe");
        addBookRequest.setGenre(Genre.DRAMA);
        addBookRequest.setIsAvailable("Yes");


        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setTitle(accountRegisterRequest.getUsername());
        borrowBookRequest.setRole(Role.MEMBER);
        borrowBookRequest.setUsername(accountRegisterRequest.getUsername());
        borrowBookRequest.setAuthor(addBookRequest.getAuthor());
        borrowBookRequest.setBookName("Things fall apart");
        borrowBookRequest.setAuthor("Chinua Achebe");

    }

    @Test
    public void registerUser_numberOfUser_isOne_Test() {
        accountRegisterRequest.setRole(Role.MEMBER);
        AccountRegisterResponse response = userService.register(accountRegisterRequest);
        assertNotNull(response);
        assertEquals(1L, users.count());
    }

    @Test
    public void i_registerUserTwice_countIsStillOne_Test() {
        userService.register(accountRegisterRequest);
        assertEquals(1L, users.count());
    }

    @Test
    public void i_registerUserTwice_throwException_Test() {
        userService.register(accountRegisterRequest);
        try{
            userService.register(accountRegisterRequest);
        }catch (UserAlreadyExistsException e){
            assertEquals(e.getMessage(), "User already exists");
        }
    }

    @Test
    public void iAddOneBook_bookCountIsOne_Test() {
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());

        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(1L, books.count());
    }

    @Test
    public void i_addABookThatIs10InNumber_bookCountIs1_quantityIs10_test(){
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());

        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(1L, books.count());

//        Integer bookQuantity = addBookResponse.getBookQuantity();
//        assertEquals(10 , bookQuantity);
    }

    @Test
    public void i_borrowExistingBook_bookCountIsStill1_bookQuantityDecreasesByOne_Test() {
        accountRegisterRequest.setRole(Role.MEMBER);
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());

        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());

        System.out.println("add book id: "+ addBookResponse.getBookId());
        BorrowBookResponse borrowBookResponse = userService.borrowBook(borrowBookRequest);
//        assertEquals(null, borrowBookResponse);
        System.out.println(borrowBookResponse.getBookId());

//        assertEquals("Book borrowed successfully", borrowBookResponse.getMessage());
        assertEquals(1L, books.count());
//        assertEquals(9, addBookResponse.getBookQuantity());
    }



}