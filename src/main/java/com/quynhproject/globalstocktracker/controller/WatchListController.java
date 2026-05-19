package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.request.WatchListRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListResponse;
import com.quynhproject.globalstocktracker.service.WatchListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watch-list")
@RequiredArgsConstructor
@Tag(name = "Watchlists", description = "Watchlist CRUD APIs")
public class WatchListController {

    private final WatchListService watchListService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllWatchList(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<WatchListResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("List watchlists")
                        .data(watchListService.getAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getByWatchListById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<WatchListResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Watchlist")
                        .data(watchListService.getById(id))
                        .build()
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<?>> getByWatchListByUserId(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<WatchListResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("User watchlists")
                        .data(watchListService.getByUser(id))
                        .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> createWatchList(@Valid @RequestBody WatchListRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<WatchListResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Watchlist created")
                        .data(watchListService.createWatchList(request))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateWatchList(@PathVariable("id") Long id, @Valid @RequestBody WatchListRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<WatchListResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Watchlist updated")
                        .data(watchListService.updateWatchList(id,request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateWatchList(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<WatchListResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Watchlist deleted")
                        .data(watchListService.deleteWatchList(id))
                        .build()
        );
    }

}
