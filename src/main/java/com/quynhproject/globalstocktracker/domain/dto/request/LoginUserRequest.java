package com.quynhproject.globalstocktracker.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {
    @NotBlank(message = "Username is required")
    @Schema(example = "user123", description = "example username")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(example = "user123", description = "example password")
    private String password;
}
