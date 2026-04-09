package com.quynhproject.globalstocktracker.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockChartResponse {
    private String symbol;
    private List<PricePoint> data;


}
