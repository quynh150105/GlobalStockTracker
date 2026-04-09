package com.quynhproject.globalstocktracker.service;

import com.quynhproject.globalstocktracker.domain.dto.response.StockChartResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.StockResponse;

import java.util.List;

public interface StockService {

    StockResponse getStockInfo(String symbol);

    StockResponse createStockFromApi(String symbol);

    StockChartResponse getStockChart(String symbol);

    List<StockResponse> getStockFromWatchList(Long WatchlistId);
}
