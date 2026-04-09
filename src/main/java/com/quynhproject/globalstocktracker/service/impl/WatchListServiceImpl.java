package com.quynhproject.globalstocktracker.service.impl;

import com.quynhproject.globalstocktracker.domain.dto.request.WatchListRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListItemStockResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListResponse;
import com.quynhproject.globalstocktracker.domain.entity.*;
import com.quynhproject.globalstocktracker.domain.mapper.WatchListMapper;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.StockRepository;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import com.quynhproject.globalstocktracker.repository.WatchListItemRepository;
import com.quynhproject.globalstocktracker.repository.WatchListRepository;
import com.quynhproject.globalstocktracker.service.StockService;
import com.quynhproject.globalstocktracker.service.WatchListService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepository watchListRepository;

    private final UserRepository userRepository;

    private final WatchListMapper watchListMapper;

    private final StockRepository  stockRepository;

    private final WatchListItemRepository  watchListItemRepository;

    private final StockService stockService;

    @Override
    public WatchListResponse createWatchList(WatchListRequest request) {

        Optional<WatchLists> watchListsOptional = watchListRepository.findByName(request.getName());

        if(watchListsOptional.isPresent()){
            throw new AppException("watch List da ton tai");
        }

        WatchLists watchLists = watchListMapper.toWatchList(request);



        return watchListMapper.toWatchListResponse(watchListRepository.save(watchLists));
    }

    @Override
    public WatchListResponse updateWatchList(Long  id, WatchListRequest request) {
        WatchLists existing = watchListRepository.findById(id)
                .orElseThrow(()-> new AppException("Watch list not found"));

        existing.setName(request.getName());
        existing.setUser(userRepository.findById(request.getUserId()).get());
        watchListRepository.save(existing);

        return watchListMapper.toWatchListResponse(existing);
    }

    @Override
    public WatchListResponse deleteWatchList(Long id) {
        WatchLists watchLists = watchListRepository.findById(id)
                .orElseThrow(() -> new AppException("Watch list not found"));

        watchListRepository.delete(watchLists);

        return watchListMapper.toWatchListResponse(watchLists);
    }

    @Override
    public WatchListResponse getById(Long id) {
        WatchLists watchLists = watchListRepository.findById(id)
                .orElseThrow(() -> new AppException("Watch list not found"));

        return watchListMapper.toWatchListResponse(watchLists);
    }

    @Override
    public List<WatchListResponse> getAll() {

        List<WatchLists> list = watchListRepository.findAll();

        return watchListMapper.toListWatchListResponse(list);
    }

    @Override
    public List<WatchListResponse> getByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException("user not found"));

        List<WatchLists> list = watchListRepository.findByUserId(userId);
        return watchListMapper.toListWatchListResponse(list);
    }

    @Override
    @Transactional
    public WatchListItemStockResponse addStockToWatchList(Long watchListId, String symbol) {

        symbol = symbol.toUpperCase();

        WatchLists watchLists = watchListRepository.findById(watchListId)
                .orElseThrow(()-> new AppException("Watch List not found"));

        String finalSymbol = symbol;
        Stock stock = stockRepository.findBySymbolWithPrices(symbol)
                .orElseGet(()->{
                    stockService.createStockFromApi(finalSymbol);
                            return stockRepository.findBySymbolWithPrices(finalSymbol)
                                    .orElseThrow(()-> new AppException("Stock not found after API call"));
                });

        boolean exist = watchListItemRepository.existsByWatchlistAndStock(watchLists,stock);

        if(exist){
            throw new AppException("Stock already in watchlist");
        }

        WatchListItem watchListItem = new WatchListItem();
        watchListItem.setWatchlist(watchLists);
        watchListItem.setStock(stock);

        watchListItemRepository.save(watchListItem);

        StockPrices latest = stock.getStockPrices()
                .stream()
                .max(Comparator.comparing(StockPrices::getTimestamp))
                .orElse(null);


        return WatchListItemStockResponse.builder()
                .watchListId(watchLists.getId())
                .symbol(stock.getSymbol())
                .latestPrice(latest != null ? latest.getPrice() : null)
                .build();

    }
}
