package com.quynhproject.globalstocktracker.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email is invalid")
    @NotBlank(message = "Email is required")
    @Schema(example = "example@gmail.com", description = "example email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(example = "user123", description = "example password")
    private String password;

    @NotBlank(message = "Username is required")
    @Schema(example = "user123", description = "example username")
    @Size(min = 3, message = "Username must be at least 6 characters")
    private String username;
}
