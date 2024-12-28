package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.jlims_refactored.enums.Genre;

import java.time.LocalDateTime;


@Setter
@Getter
public class AddBookRequest {
    private String title;
    private String author;
    private Integer noOfCopies;
    private String isAvailable;
    private Genre genre;
    private LocalDateTime dateTime;
    private String username;
}
