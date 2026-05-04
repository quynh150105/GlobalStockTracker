package com.quynhproject.globalstocktracker.domain.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Builder
public class RefreshTokenRequest {
    private String token;
}
