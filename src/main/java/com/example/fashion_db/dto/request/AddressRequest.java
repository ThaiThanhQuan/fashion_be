package com.example.fashion_db.dto.request;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {

    @Size(min = 2, message = "RECIPIENT_NAME_INVALID")
    String recipientName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "PHONE_INVALID")
    String phone;

    @Size(min = 5, message = "ADDRESS_INVALID")
    String address;

    @NotNull(message = "ADDRESS_STATUS_REQUIRED")
    Boolean active;
}
