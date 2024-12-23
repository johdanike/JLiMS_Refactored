package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Libraries extends MongoRepository<Library, String> {
    void save(Book book);
    Libraries findByUserAndBookId(User registeredMember, String bookId);
}
