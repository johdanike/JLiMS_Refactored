package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.BorrowBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.BorrowBookResponse;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
public class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private UserRepository userRepository;

    private Inventory inventory;
    private User user;
    private Library library;
    private BorrowBookRequest borrowBookRequest;
    private BorrowBookResponse borrowBookResponse;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        libraryRepository.deleteAll();
        userRepository.deleteAll();

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setNumOfCopies(4);
        book.setGenre(Genre.FICTIONS);
        bookRepository.save(book);

        user = new User();
        user.setLoggedIn(false);
        user.setRole(Role.MEMBER);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        userRepository.save(user);

        inventory = new Inventory();
        inventory.setNoOfCopyOfBooks(5);
        inventory.setReturned(false);
        inventory.setBorrowed(true);
        inventory.setDateBorrowed(LocalDate.now());
        inventoryRepository.save(inventory);



    }

    @Test
    public void bookCanBeBorrowed_test(){
        userRepository.save(user);
        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername(user.getUsername());
        borrowBookRequest.setBookId(borrowBookResponse.getBookId());

    }

}