package org.africa.semicolon.jlims.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims.Enums.Role;

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
}
