package com.quynhproject.globalstocktracker.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_prices")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockPrices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="price")
    @Min(value = 0)
    private BigDecimal price;

    @Column(name="volume")
    private Long volume;

    // thoi diem ghi nhan gia
    @Column(name="timestamp")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
}
