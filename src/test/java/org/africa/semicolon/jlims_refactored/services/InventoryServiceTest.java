package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.AddBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.request.BorrowBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.BorrowBookResponse;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private UserService userService;

    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory inventory;
    private User user;
    private Library library;
    private BorrowBookRequest borrowBookRequest;
    private BorrowBookResponse borrowBookResponse;
    private AddBookRequest addBookRequest;

    @BeforeEach
    public void setUp() {
        libraryRepository.deleteAll();
        userRepository.deleteAll();
        inventoryRepository.deleteAll();
        bookRepository.deleteAll();

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setNumOfCopies(4);
        book.setGenre(Genre.FICTION);
        bookRepository.save(book);

        user = new User();
        user.setLoggedIn(false);
        user.setRole(Role.MEMBER);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        userRepository.save(user);

        library = new Library();
        library.setBooks(bookRepository.findAll());
        library.setInventories(inventoryRepository.findAll());
        library.setUsers(userRepository.findAll());
        libraryRepository.save(library);

        inventory = new Inventory();
        inventory.setNoOfCopyOfBooks(5);
        inventory.setReturned(false);
        inventory.setBorrowed(true);
        inventory.setDateBorrowed(LocalDate.now());
    }

    @Test
    public void bookHasBeenBorrowed_ifLibrarianChecksInventory_dateBorrowed_byWho_whatWasBorrowed_canBeRetrieved_test() {
        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername(user.getUsername());
        borrowBookRequest.setBookName("Some Book Name");

        borrowBookResponse = new BorrowBookResponse();
        borrowBookResponse.setBookName(borrowBookRequest.getBookName());
        borrowBookResponse.setBookId(borrowBookRequest.getBookId());
        borrowBookResponse.setName(borrowBookRequest.getUsername());

        library.setBooks(bookRepository.findAll());
        libraryRepository.save(library);
        assertNotNull(libraryRepository.findAll());
        System.out.println(library.getInventories());

        inventory.setBookId(borrowBookResponse.getBookId());
        inventory.setUserId(user.getId());
        inventoryRepository.save(inventory);

        Inventory savedInventory = inventoryRepository.findById(inventory.getId()).orElse(null);
        assertNotNull(savedInventory);
        assertTrue(savedInventory.isBorrowed());
        assertEquals(user.getId(), savedInventory.getUserId());
        assertEquals("username", borrowBookResponse.getName());
    }

    @Test
    public void librarianCanViewNumberOfCopiesOfBookLeftAfterEachBorrow_test(){
        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername(user.getUsername());
        borrowBookRequest.setBookName("Some Book Name");

        borrowBookResponse = new BorrowBookResponse();
        borrowBookResponse.setBookName(borrowBookRequest.getBookName());
        borrowBookResponse.setBookId(borrowBookRequest.getBookId());
        borrowBookResponse.setName(borrowBookRequest.getUsername());

        library.setBooks(bookRepository.findAll());
        libraryRepository.save(library);
        assertNotNull(libraryRepository.findAll());

        inventory.setBookId(borrowBookResponse.getBookId());
        inventory.setUserId(user.getId());
        inventoryRepository.save(inventory);

        Inventory savedInventory = inventoryRepository.findById(inventory.getId()).orElse(null);
        assertNotNull(savedInventory);
        assertTrue(savedInventory.isBorrowed());
        assertEquals(user.getId(), savedInventory.getUserId());
        assertEquals("username", borrowBookResponse.getName());
        assertTrue(savedInventory.isBorrowed());
        assertFalse(savedInventory.isReturned());



    }

}
