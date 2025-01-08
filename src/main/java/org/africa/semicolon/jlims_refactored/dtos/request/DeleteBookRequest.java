package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Role;

@Setter
@Getter
public class DeleteBookRequest {
    private String bookId;
    private String message;
    private String username;
}
