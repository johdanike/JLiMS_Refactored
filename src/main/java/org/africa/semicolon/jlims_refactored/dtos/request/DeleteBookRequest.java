package org.africa.semicolon.jlims.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteBookRequest {
    private String bookId;
    private String message;
}
