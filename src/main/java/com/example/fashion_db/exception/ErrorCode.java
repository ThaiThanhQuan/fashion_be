package com.example.fashion_db.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1002, "Invalid message key", HttpStatus.BAD_REQUEST),

    // Validation Errors
    USERNAME_INVALID(1005, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1006, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1011, "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1012, "Date of birth must be in the past", HttpStatus.BAD_REQUEST),
    RECIPIENT_NAME_REQUIRED(1015, "Recipient name is required", HttpStatus.BAD_REQUEST),
    RECIPIENT_NAME_INVALID(1016, "Recipient name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1018, "Phone format is invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_INVALID(1020, "Address must be at least {min} characters", HttpStatus.BAD_REQUEST),
    ADDRESS_STATUS_REQUIRED(1021, "Address status is required", HttpStatus.BAD_REQUEST),

    // Authentication & Authorization
    UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "You do not have permission", HttpStatus.FORBIDDEN),

    // Business Logic Errors
    USER_EXISTED(1001, "User already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1003, "Role does not exist", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1004, "User does not exist", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_EXISTED(1013, "Address does not exist", HttpStatus.NOT_FOUND);



    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
