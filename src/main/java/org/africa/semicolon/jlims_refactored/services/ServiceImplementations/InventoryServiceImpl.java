package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import lombok.AllArgsConstructor;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.services.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

//    @Override
//    public boolean isBorrowed(String bookName) {
//
//        return false;
//    }
//
//    @Override
//    public boolean isReturned(String bookName) {
//        return false;
//    }

    @Override
    public Integer noOfBooks() {

        return 0;
    }

    @Override
    public void save(Inventory inventory) {

//        newInventory.setBookId(savedBook.getId());
//        newInventory.setUserId(user.getUserId());
//        newInventory.setNoOfCopyOfBooks(book.getNumOfCopies());
//        var savedInv = inventoryRepository.save(newInventory);

//        listOfBooks.add(savedBook);
//        listOfUsers.add(user);
//
//        Library library = new Library();
//        library.setBooks(listOfBooks);
//        library.setUsers(listOfUsers);
//        library.setInventories(List.of(savedInv));
        inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> findRecordOfUser(User user) {
        if(user == null) throw new IllegalArgumentException("user is null");
        return inventoryRepository.findByUserId(user.getId());
    }
}
