package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.services.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Override
    public boolean isBorrowed(String bookName) {

        return false;
    }

    @Override
    public boolean isReturned(String bookName) {
        return false;
    }

    @Override
    public Integer noOfBooks() {
        return 0;
    }

    @Override
    public void save(Inventory inventory) {

    }
}
