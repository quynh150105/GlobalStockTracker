package com.quynhproject.globalstocktracker.repository;

import com.quynhproject.globalstocktracker.domain.entity.WatchLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchListRepository extends JpaRepository<WatchLists,Long> {

    Optional<WatchLists> findByName(String name);
    List<WatchLists> findByUserId(Long userId);
}
