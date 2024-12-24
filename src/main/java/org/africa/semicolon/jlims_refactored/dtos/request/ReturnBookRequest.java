package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnBookRequest {
    private String username;
    private String bookName;
    private String author;
    private String bookId;
}
