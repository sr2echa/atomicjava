package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for user login")
public class LoginRequest {
    @NotBlank(message = "Username or email cannot be empty")
    @Schema(description = "Username or email of the user", example = "john.doe")
    private String usernameOrEmail;

    @NotBlank(message = "Password cannot be empty")
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}