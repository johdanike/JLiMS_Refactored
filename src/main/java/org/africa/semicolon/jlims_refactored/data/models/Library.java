package org.africa.semicolon.jlims_refactored.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Library {
    @Id
    private String id;
    List<Book> books;
    List<User>users;
    List<Inventory>inventories;

}
