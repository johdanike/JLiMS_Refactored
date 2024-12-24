package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnBookResponse {
    private String bookId;
    private Integer quantity;
    private String message;
    private String username;
}
