package com.quynhproject.globalstocktracker.domain.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenResponse {
    private String token;

    private boolean authenticated;
}
