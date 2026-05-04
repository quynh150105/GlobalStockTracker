package com.quynhproject.globalstocktracker.domain.dto.request;

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
    @NotBlank(message="Username không được để trống")
    private String username;

    @NotBlank(message="pasword không được để trống")
    private String password;
}
