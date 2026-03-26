package com.quynhproject.globalstocktracker.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String provider;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
