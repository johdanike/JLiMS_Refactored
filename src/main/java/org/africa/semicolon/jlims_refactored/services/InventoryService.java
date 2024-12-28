package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;

import java.util.List;

public interface InventoryService {
//    boolean isBorrowed(String bookName);
//    boolean isReturned(String bookName);
    Integer noOfBooks();
    void save(Inventory inventory);
    List<Inventory> findRecordOfUser(User user);
}
