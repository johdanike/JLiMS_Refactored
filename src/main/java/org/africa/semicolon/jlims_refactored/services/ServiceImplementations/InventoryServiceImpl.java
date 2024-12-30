package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import lombok.AllArgsConstructor;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.services.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Integer noOfBooks() {

        return 0;
    }

    @Override
    public void save(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> findRecordOfUser(User user) {
        if(user == null) throw new IllegalArgumentException("user is null");
        return inventoryRepository.findByUserId(user.getId());
    }
}
