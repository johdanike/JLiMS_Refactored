package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByUserId(String  userId);
}
