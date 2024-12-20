package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Books extends MongoRepository<Book, String> {
}
