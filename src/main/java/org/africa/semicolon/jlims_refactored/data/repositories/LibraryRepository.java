package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LibraryRepository extends MongoRepository<Library, String> {
}
