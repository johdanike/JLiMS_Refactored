package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;

@Setter
@Getter
public class BorrowBookRequest {
    private String username;
    private String bookName;
    private String author;
    private Role role;
    private String title;
    private String bookId;
}
