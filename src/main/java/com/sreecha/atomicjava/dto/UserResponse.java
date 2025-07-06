package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for User details")
public class UserResponse {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "john.doe")
    private String username;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Registration date of the user", example = "2023-01-15T14:30:00")
    private LocalDateTime registrationDate;

    @Schema(description = "Roles assigned to the user", example = "["USER"]")
    private Set<String> roles;
}