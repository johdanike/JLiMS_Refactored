package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.data.models.User;

import java.time.LocalDateTime;

@Setter
@Getter
public class AddBookResponse {
    private String bookName;
    private String message;
    private String bookId;
    private Integer bookQuantity;
    private String userId;
    private LocalDateTime createdAt;
}
