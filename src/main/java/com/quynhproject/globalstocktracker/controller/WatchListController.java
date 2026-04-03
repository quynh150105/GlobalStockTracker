package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.request.WatchListRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListResponse;
import com.quynhproject.globalstocktracker.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watch-list")
@RequiredArgsConstructor
public class WatchListController {

    private final WatchListService watchListService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllWatchList(){
        return ResponseEntity.ok(
                ApiResponse.<List<WatchListResponse>>builder()
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .message("List Watch List")
                        .data(watchListService.getAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getByWatchListById(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.<WatchListResponse>builder()
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .message("Watch List")
                        .data(watchListService.getById(id))
                        .build()
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<?>> getByWatchListByUserId(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.<List<WatchListResponse>>builder()
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .message("Watch List")
                        .data(watchListService.getByUser(id))
                        .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> createWatchList(@RequestBody WatchListRequest request){
        return ResponseEntity.ok(
                ApiResponse.<WatchListResponse>builder()
                        .HttpStatus(HttpStatus.CREATED.value())
                        .message("Watch List Created")
                        .data(watchListService.createWatchList(request))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateWatchList(@PathVariable("id") Long id, @RequestBody WatchListRequest request){
        return ResponseEntity.ok(
                ApiResponse.<WatchListResponse>builder()
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .message("Update Watch List")
                        .data(watchListService.updateWatchList(id,request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateWatchList(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.<WatchListResponse>builder()
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .message("Delete Watch List")
                        .data(watchListService.deleteWatchList(id))
                        .build()
        );
    }

}