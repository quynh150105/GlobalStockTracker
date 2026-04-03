package com.quynhproject.globalstocktracker.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class WatchListResponse {
    private Long id;
    private  String name;
    private Long userId;
}
