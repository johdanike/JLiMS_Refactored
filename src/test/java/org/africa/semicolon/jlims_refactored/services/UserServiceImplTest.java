package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.*;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.africa.semicolon.jlims_refactored.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    AccountRegisterRequest accountRegisterRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository books;
    private AddBookRequest addBookRequest;
    private BorrowBookRequest borrowBookRequest;
    private ReturnBookRequest returnBookRequest;
    @Autowired
    private LibraryRepository libraryRepo;
    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        books.deleteAll();
        libraryRepo.deleteAll();
        inventoryRepository.deleteAll();

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
        addBookRequest.setNoOfCopies(10);


        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername("username");
        borrowBookRequest.setRole(Role.MEMBER);
        borrowBookRequest.setBookName("Things fall apart");
        borrowBookRequest.setAuthor("Chinua Achebe");

        returnBookRequest = new ReturnBookRequest();
        returnBookRequest.setUsername("username");
        returnBookRequest.setBookName("Things fall apart");
        returnBookRequest.setAuthor("Chinua Achebe");

    }

    @Test
    public void registerUser_numberOfUser_isOne_Test() {
        accountRegisterRequest.setRole(Role.MEMBER);
        AccountRegisterResponse response = userService.register(accountRegisterRequest);
        assertNotNull(response);
        assertEquals(1L, userRepository.count());
    }

    @Test
    public void i_registerUserTwice_countIsStillOne_Test() {
        userService.register(accountRegisterRequest);
        assertEquals(1L, userRepository.count());
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

        Integer bookQuantity = addBookResponse.getBookQuantity();
        assertEquals(10 , bookQuantity);
    }

    @Test
    public void i_borrowExistingBook_bookCountIsStill1_bookQuantityDecreasesByOne_Test() {
        accountRegisterRequest.setRole(Role.MEMBER);
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());
        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(10, addBookResponse.getBookQuantity());

        borrowBookRequest.setBookId(addBookResponse.getBookId());
        BorrowBookResponse borrowBookResponse = userService.borrowBook(borrowBookRequest);
        assertEquals("Book borrowed successfully", borrowBookResponse.getMessage());
        assertEquals(1L, books.count());
        assertEquals(9, borrowBookResponse.getQuantity());

    }

    @Test
    public void i_borrowExistingBook_bookCountIsStill1_bookQuantityDecreasesByOne_iReturnBook_noOfCopiesOfBookIncreasesBy1_Test() {
        accountRegisterRequest.setRole(Role.MEMBER);
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());
        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(10, addBookResponse.getBookQuantity());

        borrowBookRequest.setBookId(addBookResponse.getBookId());
        BorrowBookResponse borrowBookResponse = userService.borrowBook(borrowBookRequest);
        assertEquals("Book borrowed successfully", borrowBookResponse.getMessage());
        assertEquals(1L, books.count());
        assertEquals(9, borrowBookResponse.getQuantity());

        returnBookRequest.setBookId(addBookResponse.getBookId());
        ReturnBookResponse returnBookResponse = userService.returnBook(returnBookRequest);
        assertEquals("Book returned successfully", returnBookResponse.getMessage());
        assertEquals(1L, books.count());
        assertEquals(10, returnBookResponse.getQuantity());
    }

    @Test
    public void librarianCanDeleteBookFromLibrary_test(){
        accountRegisterRequest.setRole(Role.LIBRARIAN);
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());
        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(10, addBookResponse.getBookQuantity());

        DeleteBookRequest deleteBookRequest = new DeleteBookRequest();
        deleteBookRequest.setBookId(addBookResponse.getBookId());
        deleteBookRequest.setUsername("username");
        deleteBookRequest.setMessage("Book deleted successfully");
        DeleteBookResponse deleteBookResponse = userService.deleteBook(deleteBookRequest);
        assertEquals("Book deleted successfully", deleteBookResponse.getMessage());
        assertEquals(0L, books.count());
    }

    @Test
    public void librarianSearchForUser_canViewAllBooksBorrowed_test(){
        accountRegisterRequest.setRole(Role.LIBRARIAN);
        userService.register(accountRegisterRequest);
        assertEquals(0L, books.count());
        AddBookResponse addBookResponse = userService.addBook(addBookRequest);
        assertEquals("Book added successfully", addBookResponse.getMessage());
        assertEquals(10, addBookResponse.getBookQuantity());

        List<Book> booksBorrowedByUser = userService.findBooksBorrowedByMember("username");
        assertNotNull(booksBorrowedByUser);

    }

}