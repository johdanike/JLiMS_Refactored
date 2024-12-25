package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.BorrowBookRequest;
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
    private BorrowBookRequest borrowBookRequest;

    @BeforeEach
    public void setUp() {
        libraryRepository.deleteAll();
        userRepository.deleteAll();

        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername("Lagbaja");


    }

    @Test
    public void bookCanBeBorrowed_test(){

    }

}