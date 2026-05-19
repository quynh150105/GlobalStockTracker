package com.quynhproject.globalstocktracker.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchListRequest {
    @NotBlank(message = "Watchlist name is required")
    @Size(min = 3, message = "Username must be at least 6 characters")
    @Schema(example = "user123", description = "example username")
    private  String name;

    @NotNull(message = "User id is required")
    @Schema(example = "1", description = "example userId")
    private Long userId;
}
