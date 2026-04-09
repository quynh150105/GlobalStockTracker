package com.quynhproject.globalstocktracker.service.impl;

import com.quynhproject.globalstocktracker.constant.Currency;
import com.quynhproject.globalstocktracker.domain.dto.response.AlphaVantageChartResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.PricePoint;
import com.quynhproject.globalstocktracker.domain.dto.response.StockChartResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.StockResponse;
import com.quynhproject.globalstocktracker.domain.entity.Stock;
import com.quynhproject.globalstocktracker.domain.entity.StockPrices;
import com.quynhproject.globalstocktracker.domain.entity.WatchListItem;
import com.quynhproject.globalstocktracker.domain.entity.WatchLists;
import com.quynhproject.globalstocktracker.domain.mapper.StockMapper;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.StockRepository;
import com.quynhproject.globalstocktracker.repository.WatchListItemRepository;
import com.quynhproject.globalstocktracker.repository.WatchListRepository;
import com.quynhproject.globalstocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final WatchListRepository watchListRepository;
    private final WatchListItemRepository watchListItemRepository;
    private final StockMapper stockMapper;
    private final RestTemplate restTemplate;

    @Value("${alphavantage.api-key}")
    private String API_KEY;

    @Override
    public StockResponse getStockInfo(String symbol) {
        Stock stock = stockRepository.findBySymbolWithPrices(symbol)
                .orElseThrow(() -> new AppException("Stock not found"));
        return stockMapper.toStockResponse(stock);
    }

    @Override
    public StockResponse createStockFromApi(String symbol) {
        symbol = symbol.toUpperCase();

        if (stockRepository.findBySymbolWithPrices(symbol).isPresent()) {
            throw new AppException("Stock already exists");
        }

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
                + "&symbol=" + symbol
                + "&apikey=" + API_KEY;

        AlphaVantageChartResponse response = restTemplate.getForObject(url, AlphaVantageChartResponse.class);
        if (response == null || response.getTimeSeries() == null) {
            throw new AppException("Stock not found in API");
        }

        String latestDate = response.getTimeSeries().keySet()
                .stream()
                .max(String::compareTo)
                .orElseThrow(() -> new AppException("No time series data found"));

        AlphaVantageChartResponse.TimeSeries latestData = response.getTimeSeries().get(latestDate);

//        double latestClosePrice = Double.parseDouble(
//                Optional.ofNullable(latestData.getClose()).orElse("0")
//        );

        Stock stock = Stock.builder()
                .symbol(symbol)
                .name(symbol)
                .currency(Currency.USD)
                .createdAt(LocalDateTime.now())// API không cung cấp tên, tạm thời dùng symbol làm name
                .build();

        List<StockPrices> prices = response.getTimeSeries().entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey())) // sắp xếp theo ngày giảm dần
                .limit(30)
                .map(entry -> StockPrices.builder()
                        .stock(stock)
                        .price(new BigDecimal(entry.getValue().getClose()))
                        .timestamp(LocalDate.parse(entry.getKey()).atStartOfDay())
                        .build())
                .toList();

        stock.setStockPrices(prices);

        stockRepository.save(stock);

        return stockMapper.toStockResponse(stock);

    }

    @Override
    public StockChartResponse getStockChart(String symbol) {
        try {
            symbol = symbol.toUpperCase();

            String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
                    + "&symbol=" + symbol
                    + "&apikey=" + API_KEY;


            AlphaVantageChartResponse response = restTemplate.getForObject(url, AlphaVantageChartResponse.class);

            if (response == null || response.getTimeSeries() == null) {
                throw new AppException("no chart data");
            }
            List<PricePoint> points = response.getTimeSeries().entrySet()
                    .stream()
                    .map(entry -> PricePoint.builder()
                            .time(entry.getKey())
                            .openPrice(Double.parseDouble(entry.getValue().getOpen()))
                            .highPrice(Double.parseDouble(entry.getValue().getHigh()))
                            .lowPrice(Double.parseDouble(entry.getValue().getLow()))
                            .closePrice(Double.parseDouble(entry.getValue().getClose()))
                            .build()
                    ).sorted(Comparator.comparing(PricePoint::getTime).reversed())
                    .limit(30)
                    .toList();
            return StockChartResponse.builder()
                    .data(points)
                    .symbol(symbol)
                    .build();
        } catch (Exception e) {
            throw new AppException("Error fetching data");
        }
    }

    @Override
    public List<StockResponse> getStockFromWatchList(Long WatchlistId) {
        WatchLists watchLists = watchListRepository.findById(WatchlistId)
                .orElseThrow(() -> new AppException("Watch List not found"));

        List<WatchListItem> items = watchListItemRepository.findByWatchListIdWithStock(WatchlistId);

        return items.stream()
                .map(WatchListItem::getStock)
                .map(stockMapper::toStockResponse)
                .toList();
    }
}
