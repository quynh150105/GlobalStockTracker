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
import org.springframework.web.util.UriComponentsBuilder;

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
        symbol = normalizeSymbol(symbol);
        Stock stock = stockRepository.findBySymbolWithPrices(symbol)
                .orElseThrow(() -> new AppException("Stock not found"));
        return stockMapper.toStockResponse(stock);
    }

    @Override
    public StockResponse createStockFromApi(String symbol) {
        symbol = normalizeSymbol(symbol);

        if (stockRepository.findBySymbolWithPrices(symbol).isPresent()) {
            throw new AppException("Stock already exists");
        }

        AlphaVantageChartResponse response = fetchDailyTimeSeries(symbol);

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
        symbol = normalizeSymbol(symbol);

        AlphaVantageChartResponse response;
        try {
            response = fetchDailyTimeSeries(symbol);
        } catch (AppException ex) {
            return getStockChartFromDatabase(symbol, ex);
        }

        List<PricePoint> points = response.getTimeSeries().entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .limit(30)
                .map(entry -> PricePoint.builder()
                        .time(entry.getKey())
                        .openPrice(Double.parseDouble(entry.getValue().getOpen()))
                        .highPrice(Double.parseDouble(entry.getValue().getHigh()))
                        .lowPrice(Double.parseDouble(entry.getValue().getLow()))
                        .closePrice(Double.parseDouble(entry.getValue().getClose()))
                        .build()
                )
                .sorted(Comparator.comparing(PricePoint::getTime))
                .toList();

        return StockChartResponse.builder()
                .data(points)
                .symbol(symbol)
                .build();
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

    private String normalizeSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new AppException("Symbol is required");
        }
        return symbol.trim().toUpperCase();
    }

    private AlphaVantageChartResponse fetchDailyTimeSeries(String symbol) {
        String url = UriComponentsBuilder.fromUriString("https://www.alphavantage.co/query")
                .queryParam("function", "TIME_SERIES_DAILY")
                .queryParam("symbol", symbol)
                .queryParam("apikey", API_KEY)
                .toUriString();

        AlphaVantageChartResponse response = restTemplate.getForObject(url, AlphaVantageChartResponse.class);

        if (response == null) {
            throw new AppException("No chart data");
        }
        if (response.getErrorMessage() != null) {
            throw new AppException("Invalid stock symbol");
        }
        if (response.getNote() != null || response.getInformation() != null) {
            throw new AppException("Alpha Vantage API limit reached");
        }
        if (response.getTimeSeries() == null || response.getTimeSeries().isEmpty()) {
            throw new AppException("No chart data");
        }

        return response;
    }

    private StockChartResponse getStockChartFromDatabase(String symbol, AppException originalException) {
        Optional<Stock> stockOptional = stockRepository.findBySymbolWithPrices(symbol);

        if (stockOptional.isEmpty()
                || stockOptional.get().getStockPrices() == null
                || stockOptional.get().getStockPrices().isEmpty()) {
            throw originalException;
        }

        List<PricePoint> points = stockOptional.get().getStockPrices()
                .stream()
                .filter(stockPrice -> stockPrice.getPrice() != null && stockPrice.getTimestamp() != null)
                .sorted(Comparator.comparing(StockPrices::getTimestamp).reversed())
                .limit(30)
                .map(stockPrice -> {
                    Double price = stockPrice.getPrice().doubleValue();
                    return PricePoint.builder()
                            .time(stockPrice.getTimestamp().toLocalDate().toString())
                            .openPrice(price)
                            .highPrice(price)
                            .lowPrice(price)
                            .closePrice(price)
                            .build();
                })
                .sorted(Comparator.comparing(PricePoint::getTime))
                .toList();

        if (points.isEmpty()) {
            throw originalException;
        }

        return StockChartResponse.builder()
                .symbol(symbol)
                .data(points)
                .build();
    }
}
