package com.quynhproject.globalstocktracker.domain.entity;

import com.quynhproject.globalstocktracker.constant.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ma co phieu
    @Column(name="symbol", unique = true, nullable = false, length = 10)
    private String symbol;

    @Column(name="name") // ten cong ty
    private String name;

    @Column(name="exchange", length = 50) // san giao dich
    private String exchange;

    @Enumerated(EnumType.STRING)
    @Column(name="currency", length = 10) // don vi tien
    private Currency currency;

    @Column(name="createAt")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockPrices> stockPrices;
}
