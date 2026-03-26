package com.quynhproject.globalstocktracker.domain.dto.request;

import com.quynhproject.globalstocktracker.constant.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String username;

    private String password;

}
