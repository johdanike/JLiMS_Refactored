package org.africa.semicolon.jlims.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddBookResponse {
    private String bookName;
    private String message;
    private Integer bookQuantity;
    private String bookId;
}
