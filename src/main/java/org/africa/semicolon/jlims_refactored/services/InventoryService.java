package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Inventory;

public interface InventoryService {
    boolean isBorrowed(String bookName);
    boolean isReturned(String bookName);
    Integer noOfBooks();
    void save(Inventory inventory);

}
