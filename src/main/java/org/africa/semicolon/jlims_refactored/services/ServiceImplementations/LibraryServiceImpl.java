package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.africa.semicolon.jlims_refactored.services.LibraryService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public List<Book> viewAllBooks() {
       List<Book> foundBooks = bookRepository.findAll();
        if (foundBooks.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        log.info("Found Books :::>> {}", foundBooks);
        return foundBooks;
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        List<Book> foundBooks = bookRepository.findByGenre(Genre.valueOf(String.valueOf(genre)));
        if (foundBooks.isEmpty()) throw new IllegalArgumentException("No book found with genre " + genre);
        log.info("Found Books :::>> {}", foundBooks);
        return foundBooks;
    }

    @Override
    public List<Book> findBooksByTitle(String bookTitle) {
        if (bookTitle.isEmpty()) throw new IllegalArgumentException("Title not found");
        List<Book> foundBooks = bookRepository.findBookByTitle(bookTitle);
        log.info("Found Books :::>> {}", foundBooks);
        return foundBooks;
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        if (author.isEmpty()) throw new IllegalArgumentException("Author not found");
        List<Book> foundBooks = bookRepository.findBookByAuthor(author);
        log.info("found Books :::>> {}", foundBooks);
        return foundBooks;
    }

    @Override
    public List<User> viewAllUsers() {
        if (userRepository.findAll().isEmpty()) throw new IllegalArgumentException("User not found");
        log.info("Found users :::>> {}", userRepository.findAll());
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        User user = getExactUser(username);
        assert user != null;
        checkIfUserIsALibrarian(user.getRole());
        if (userRepository.findAll().isEmpty()) throw new IllegalArgumentException("No user has been registered");
        User foundUser = userRepository.findByUsername(username);
        log.info("Found user :::>> {}", foundUser);
        return foundUser;
    }

    @Override
    public User deleteUser(String username) {
        User user = getExactUser(username);
        assert user != null;
//        checkIfUserIsALibrarian(user.getRole());
//        List<Inventory> stats = checkStatOfBookBorrowedByUser(user);
//        if (stats.isEmpty()) {
//            throw new IllegalArgumentException("No inventory found for user " + username);
//        }
//        Inventory inventory = new Inventory();
//        if (inventory.isBorrowed())
//            throw new IllegalArgumentException("User is already borrowed");
//        else userRepository.delete(user);
        userRepository.delete(user);
        return user;
    }

    private void checkIfUserIsALibrarian(Role role){
        if (!role.equals(Role.LIBRARIAN)) throw new IllegalArgumentException("Access denied");
    }

    private List<Inventory> checkStatOfBookBorrowedByUser(User user){
         List<Inventory> records = inventoryRepository.findRecordOfUser(user);
         if (records.isEmpty()) throw new IllegalArgumentException("No inventory found");
         return records;
    }

    private User getExactUser(String username){
        User foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isLoggedIn())
            return foundUsername;
        return null;
    }
}
