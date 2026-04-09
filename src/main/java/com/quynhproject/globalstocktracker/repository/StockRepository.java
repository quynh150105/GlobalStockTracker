package com.quynhproject.globalstocktracker.repository;

import com.quynhproject.globalstocktracker.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {
    @Query("SELECT s FROM Stock s LEFT JOIN FETCH s.stockPrices WHERE s.symbol = :symbol")
    Optional<Stock> findBySymbolWithPrices(String symbol);
}
