package com.quynhproject.globalstocktracker.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Builder
public class WatchListItemStockResponse {
    private Long watchListId;
    private String symbol;
    private BigDecimal latestPrice;
}
