package com.quynhproject.globalstocktracker.repository;

import com.quynhproject.globalstocktracker.domain.entity.Stock;
import com.quynhproject.globalstocktracker.domain.entity.WatchListItem;
import com.quynhproject.globalstocktracker.domain.entity.WatchLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListItemRepository extends JpaRepository<WatchListItem,Long> {
    boolean existsByWatchlistAndStock(WatchLists watchLists, Stock stock);

    @Query("""
    SELECT wli FROM WatchListItem wli
    JOIN FETCH wli.stock s
    LEFT JOIN FETCH s.stockPrices
    WHERE wli.watchlist.id = :watchListId
""")
    List<WatchListItem> findByWatchListIdWithStock(Long watchListId);
}
