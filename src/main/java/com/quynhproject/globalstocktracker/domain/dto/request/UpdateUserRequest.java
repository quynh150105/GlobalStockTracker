package com.quynhproject.globalstocktracker.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @Email(message = "Email is invalid")
    @Schema(example = "user123@gmail.com", description = "example email")
    private String email;
    @Schema(example = "user123", description = "example username")
    @Size(min = 3, message = "Username must be at least 6 characters")
    private String username;
    @Schema(example = "user123", description = "example password")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
