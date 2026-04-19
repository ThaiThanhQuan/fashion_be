package com.example.fashion_db.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USER_REQUIRED")
    @Size(min = 5, message = "Usernames must have at least 5 characters.")
    String username;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    String email;

    @Size(min = 8, message = "Password must have at least 8 characters.")
    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;

    @Past(message = "DOB_INVALID")
    LocalDate dob;
    boolean gender;
}
