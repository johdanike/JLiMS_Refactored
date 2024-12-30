package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    String findByTitle(String title);
    String findByAuthor(String author);
    List<Book> findByGenre(Genre genre);
    List<Book> findBookByTitle(String bookTitle);
    List<Book> findBookByAuthor(String author);
    List<Book> getBooksBorrowedBy(String id);
}
