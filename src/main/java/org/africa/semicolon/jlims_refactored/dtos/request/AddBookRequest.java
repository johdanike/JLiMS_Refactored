package org.africa.semicolon.jlims.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims.Enums.BookType;

@Setter
@Getter
public class AddBookRequest {
    private String title;
    private String author;
    private BookType bookType;
    private Integer quantity;
    private String isAvailable;
}
