package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for successful user login")
public class LoginResponse {
    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1Ni...")
    private String token;

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "john.doe")
    private String username;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Roles assigned to the user", example = "["USER"]")
    private Set<String> roles;
}