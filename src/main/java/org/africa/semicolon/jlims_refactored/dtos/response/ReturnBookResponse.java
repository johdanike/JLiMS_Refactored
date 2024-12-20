package org.africa.semicolon.jlims.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnBookResponse {
    private String bookId;
    private String libraryBookLoanId;
    private String message;
}
