package com.quynhproject.globalstocktracker.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "watch_lists")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WatchLists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "watchlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WatchListItem> items;





}
