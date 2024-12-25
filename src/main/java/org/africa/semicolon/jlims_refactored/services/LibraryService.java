package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.enums.Genre;

import java.util.List;

public interface LibraryService {
    List<Book> viewAllBooks();
    List<Book> findByGenre(Genre genre);
    List<Book> findBooksByTitle(String bookTitle);
    List<Book> findBooksByAuthor(String author);
    List<User> viewAllUsers();
}
