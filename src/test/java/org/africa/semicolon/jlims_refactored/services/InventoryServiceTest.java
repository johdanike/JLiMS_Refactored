package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.BorrowBookRequest;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    public void setUp() {
        libraryRepository.deleteAll();
        userRepository.deleteAll();


        user = new User();
        user.setLoggedIn(false);
        user.setRole(Role.LIBRARIAN);

        inventory = new Inventory();
        inventory.setNoOfCopyOfBooks(5);

    }

    @Test
    public void bookCanBeBorrowed_test(){
        userRepository.save(user);

    }

}