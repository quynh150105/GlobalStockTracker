package com.quynhproject.globalstocktracker.domain.mapper;

import com.quynhproject.globalstocktracker.domain.dto.request.WatchListRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.WatchListResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.domain.entity.WatchLists;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WatchListMapper {
    @Mapping(source = "user.id", target = "userId")
    WatchListResponse toWatchListResponse(WatchLists watchLists);

    @Mapping(source="userId", target="user.id")
    WatchLists toWatchList(WatchListRequest dto);

    default User map(Long userId){
        if(userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    List<WatchListResponse> toListWatchListResponse(List<WatchLists> list);


}
