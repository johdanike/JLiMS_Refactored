package org.africa.semicolon.jlims_refactored.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserApiResponse {
    boolean isSuccess;
    Object accountRegisterResponse;
}