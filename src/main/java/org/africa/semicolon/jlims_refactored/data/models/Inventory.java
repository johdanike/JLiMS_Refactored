package org.africa.semicolon.jlims_refactored.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Inventory {
    @Id
    private String id;
    private String userId;
    private String bookId;
    private Integer noOfCopyOfBooks;
    private LocalDateTime dateBorrowed;
    private LocalDateTime dateReturned;
    private boolean isBorrowed;
    private boolean isReturned;
}
