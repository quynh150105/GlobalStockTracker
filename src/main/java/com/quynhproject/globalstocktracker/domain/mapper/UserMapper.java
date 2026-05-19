package com.quynhproject.globalstocktracker.domain.mapper;

import com.quynhproject.globalstocktracker.constant.AuthProvider;
import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    User toUser(CreateUserRequest request);

    @Mapping(source = "authProvider", target = "provider")
    UserResponse toCreateUserResponse(User user);

    default String map(AuthProvider provider){
        return provider != null ? provider.name() : null;
    }

    List<UserResponse> toListUserResponse(List<User> userList);


    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);

}
