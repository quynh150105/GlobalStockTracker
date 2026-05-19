package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.StockResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListItemStockResponse;
import com.quynhproject.globalstocktracker.service.StockService;
import com.quynhproject.globalstocktracker.service.WatchListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
@Tag(name = "Stocks", description = "Stock quote, chart, and watchlist stock APIs")
public class StockController {

    private final StockService stockService;
    private final WatchListService watchListService;

    @GetMapping("/chart")
    public ResponseEntity<ApiResponse<?>> getChart(@RequestParam String symbol){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get stock chart")
                        .data(stockService.getStockChart(symbol))
                        .build()
        );
    }

    @GetMapping("/info")// get stock info by symbol
    public ResponseEntity<ApiResponse<StockResponse>> getStockInfo(@RequestParam String symbol){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<StockResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get stock info")
                        .data(stockService.getStockInfo(symbol))
                        .build()
        );
    }

    @GetMapping("/watchlist/{id}")// get stock in watchList id
    public ResponseEntity<ApiResponse<?>> getStockFromWatchList(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<StockResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get stocks from watchlist")
                        .data(stockService.getStockFromWatchList(id))
                        .build()
        );
    }

    @PostMapping("/{id}/stocks")//  add to watchList id
    public ResponseEntity<ApiResponse<?>> addStockToWatchList(@PathVariable("id") Long id, @RequestParam String symbol){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<WatchListItemStockResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Add stock to watchlist")
                        .data(watchListService.addStockToWatchList(id,symbol))
                        .build()
        );
    }

}
