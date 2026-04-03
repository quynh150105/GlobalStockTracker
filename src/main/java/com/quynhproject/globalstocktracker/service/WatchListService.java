package com.quynhproject.globalstocktracker.service;

import com.quynhproject.globalstocktracker.domain.dto.request.WatchListRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListResponse;
import com.quynhproject.globalstocktracker.domain.entity.WatchLists;

import java.util.ArrayList;
import java.util.List;

public interface WatchListService {
    WatchListResponse createWatchList(WatchListRequest request);

    WatchListResponse updateWatchList(Long id, WatchListRequest request);

    WatchListResponse deleteWatchList(Long id);

    WatchListResponse getById(Long id);

    List<WatchListResponse> getAll();

    List<WatchListResponse> getByUser(Long userId);

}
