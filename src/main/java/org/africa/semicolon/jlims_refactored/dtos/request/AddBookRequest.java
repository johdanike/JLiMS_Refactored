package org.africa.semicolon.jlims_refactored.dtos.request;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AddBookRequest {
    private String title;
    private String author;
    private Integer quantity;
    private String isAvailable;
}
