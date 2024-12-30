package org.africa.semicolon.jlims_refactored.data.repositories;

import org.africa.semicolon.jlims_refactored.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
     User findByUsername(String username);

}
