package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.StockResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListItemStockResponse;
import com.quynhproject.globalstocktracker.service.StockService;
import com.quynhproject.globalstocktracker.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;
    private final WatchListService watchListService;

    @GetMapping("/chart")
    public ResponseEntity<?> getChart(@RequestParam String symbol){
        return ResponseEntity.ok(
                stockService.getStockChart(symbol)
        );
    }

    @GetMapping("/info")// get stock info by symbol
    public ResponseEntity<ApiResponse<StockResponse>> getStockInfo(@RequestParam String symbol){
        return ResponseEntity.ok(
                ApiResponse.<StockResponse>builder()
                        .HttpStatus(HttpStatus.OK.value())
                        .message("get stock info")
                        .data(stockService.getStockInfo(symbol))
                        .build()
        );
    }

    @GetMapping("/watchlist/{id}")// get stock in watchList id
    public ResponseEntity<ApiResponse<?>> getStockFromWatchList(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.<List<StockResponse>>builder()
                        .HttpStatus(HttpStatus.OK.value())
                        .message("get stock from watchlist")
                        .data(stockService.getStockFromWatchList(id))
                        .build()
        );
    }

    @PostMapping("/{id}/stocks")//  add to watchList id
    public ResponseEntity<ApiResponse<?>> addStockToWatchList(@PathVariable("id") Long id, @RequestParam String symbol){
        return ResponseEntity.ok(
                ApiResponse.<WatchListItemStockResponse>builder()
                        .HttpStatus(HttpStatus.CREATED.value())
                        .message("add stock to watchlist")
                        .data(watchListService.addStockToWatchList(id,symbol))
                        .build()
        );
    }

}
