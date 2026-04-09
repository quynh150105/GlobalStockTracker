package com.quynhproject.globalstocktracker.domain.mapper;

import com.quynhproject.globalstocktracker.domain.dto.response.StockResponse;
import com.quynhproject.globalstocktracker.domain.entity.Stock;
import com.quynhproject.globalstocktracker.domain.entity.StockPrices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "latestPrice", expression = "java(getLatestPrice(stock))")
    @Mapping(target = "latestTime", expression = "java(getLatestTime(stock))")
    StockResponse toStockResponse(Stock stock);

    default BigDecimal getLatestPrice(Stock stock){
        if(stock.getStockPrices() == null || stock.getStockPrices().isEmpty()){
            return null;
        }
        return stock.getStockPrices().stream()
                .max(Comparator.comparing(StockPrices::getTimestamp))
                .map(StockPrices::getPrice)
                .orElse(null);
    }

    default LocalDateTime getLatestTime(Stock stock){
        if(stock.getStockPrices() == null || stock.getStockPrices().isEmpty()){
            return null;
        }
        return stock.getStockPrices().stream()
                .max(Comparator.comparing(StockPrices::getTimestamp))
                .map(StockPrices::getTimestamp)
                .orElse(null);
    }

}
