package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Role;

import java.time.LocalDate;


@Setter
@Getter
public class BorrowBookResponse {
    private String name;
    private String bookId;
    private String bookName;
    private String author;
    private String message;
    private Integer quantity;
    private Role role;
    private LocalDate borrowDate;
}
