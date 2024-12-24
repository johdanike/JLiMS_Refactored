package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddBookResponse {
    private String bookName;
    private String message;
    private String bookId;
    private Integer bookQuantity;
}
