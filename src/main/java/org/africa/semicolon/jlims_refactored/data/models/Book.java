package org.africa.semicolon.jlims_refactored.data.models;

import lombok.Data;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private Genre genre;
    private Integer NumOfCopies;
    private String userId;
}
