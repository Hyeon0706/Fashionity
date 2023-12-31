package com.infinity.fashionity.global.dto;

import com.infinity.fashionity.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ErrorResponse{

    private String code;
    private String message;

    public static ErrorResponse of (ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage());
    }
}
