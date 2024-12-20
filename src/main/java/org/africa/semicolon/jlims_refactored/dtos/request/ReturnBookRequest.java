package org.africa.semicolon.jlims.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims.data.models.Book;
import org.africa.semicolon.jlims.data.models.Borrower;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReturnBookRequest {
    private String username;
    private String libraryBookLoanId;
}
