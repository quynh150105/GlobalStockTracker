package com.quynhproject.globalstocktracker.domain.dto.request;

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

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message="Email không đúng định dạng")
    @NotBlank(message="Email không được để trống")
    private String email;

    @NotBlank(message="pasword không được để trống")
    @Size(min=6, message="Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message="username không đuợc để trống")
    private String username;
}
