package com.quynhproject.globalstocktracker.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlphaVantageChartResponse {
    @JsonProperty("Time Series (Daily)")
    private Map<String, TimeSeries> timeSeries;

    @JsonProperty("Error Message")
    private String errorMessage;

    @JsonProperty("Note")
    private String note;

    @JsonProperty("Information")
    private String information;

    @Data
    public static class TimeSeries{
        @JsonProperty("1. open")
        private String open;

        @JsonProperty("2. high")
        private String high;

        @JsonProperty("3. low")
        private String low;

        @JsonProperty("4. close")
        private String close;
    }
}
