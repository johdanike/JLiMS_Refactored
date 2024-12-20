package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Libraries extends MongoRepository<Library, String> {
}
