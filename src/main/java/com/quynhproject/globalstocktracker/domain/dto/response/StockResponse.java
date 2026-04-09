package com.quynhproject.globalstocktracker.domain.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {

    private String symbol;

    private String name;

    private BigDecimal latestPrice;

    private LocalDateTime latestTime;

}
