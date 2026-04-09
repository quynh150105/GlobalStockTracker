package com.quynhproject.globalstocktracker.domain.dto.response;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricePoint {
    private String time;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
}
