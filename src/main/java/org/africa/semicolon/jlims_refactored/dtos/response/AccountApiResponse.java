package org.africa.semicolon.jlims.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
public class AccountApiResponse{
    boolean isSuccess;
    Object accountRegisterResponse;
}
