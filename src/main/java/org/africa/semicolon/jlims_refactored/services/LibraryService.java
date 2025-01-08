package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;

import java.util.List;
import java.util.Optional;

public interface LibraryService {
    List<Book> viewAllBooks();
    List<Book> findByGenre(Genre genre);
    List<Book> findBooksByTitle(String bookTitle);
    List<Book> findBooksByAuthor(String author);
    List<User> viewAllUsers();
    User findUserByUsername(String username);
    List<Inventory> deleteUser(String username, User userId);
    List<Inventory> getInventory(Role role, String userId);
    List<Inventory> viewAllBooksBorrowedByUser(String username);
}
